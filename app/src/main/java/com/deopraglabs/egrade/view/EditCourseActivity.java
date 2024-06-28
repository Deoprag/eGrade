package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.adapter.SelectedSubjectsAdapter;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Course;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Subject;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EditCourseActivity extends AppCompatActivity {

    private Course course;
    private Coordinator coordinator;

    private TextView textId;
    private EditText nameEditText, descriptionEditText;
    private Button deleteButton, saveButton;
    private Spinner coordinatorSpinner;
    private List<Coordinator> coordinatorList;
    private Coordinator selectedCoordinator;
    private Spinner subjectsSpinner;
    private RecyclerView selectedSubjectsRecyclerView;
    private List<Subject> subjects;
    private List<Subject> selectedSubjects;
    private SelectedSubjectsAdapter selectedSubjectsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        Intent intent = getIntent();
        if (intent != null) {
            course = (Course) intent.getSerializableExtra("course");
            coordinator = (Coordinator) intent.getSerializableExtra("coordinator");
        }

        nameEditText = findViewById(R.id.nameEditText);
        textId = findViewById(R.id.textId);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);
        coordinatorSpinner = findViewById(R.id.coordinatorSpinner);

        loadCoordinators();
        loadSubjects();

        if (course != null) {
            textId.setText("ID: " + course.getId());
            nameEditText.setText(course.getName());
            descriptionEditText.setText(course.getDescription());
            coordinatorSpinner.setSelection(getCoordinatorIndex(course.getCoordinator()));

        } else {
            textId.setVisibility(View.INVISIBLE);
            deleteButton.setEnabled(false);
            deleteButton.setVisibility(View.INVISIBLE);
        }

        deleteButton.setOnClickListener(v -> deleteCourse());
        saveButton.setOnClickListener(v -> saveCourse());

        subjectsSpinner = findViewById(R.id.subjectsSpinner);
        selectedSubjectsRecyclerView = findViewById(R.id.selectedSubjectsRecyclerView);

        selectedSubjects = new ArrayList<>();
        selectedSubjectsAdapter = new SelectedSubjectsAdapter(selectedSubjects, subject -> {
            selectedSubjects.remove(subject);
            subjects.add(subject);
            updateSpinners();
        });

        selectedSubjectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedSubjectsRecyclerView.setAdapter(selectedSubjectsAdapter);
    }

    private void updateSpinners() {
        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectsSpinner.setAdapter(adapter);

        subjectsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Subject selectedSubject = (Subject) parent.getItemAtPosition(position);
                if (!selectedSubjects.contains(selectedSubject)) {
                    selectedSubjects.add(selectedSubject);
                    subjects.remove(selectedSubject);
                    selectedSubjectsAdapter.notifyDataSetChanged();
                    updateSpinners();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadSubjects() {
        final String url = EGradeUtil.URL + "/api/v1/subject/findAll/";

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response", response);
                final Gson gson = new Gson();
                Type subjectListType = new TypeToken<List<Subject>>() {}.getType();
                subjects = gson.fromJson(response, subjectListType);

                runOnUiThread(() -> {
                    subjects.removeAll(selectedSubjects);
                    updateSpinners();
                });
            }

            @Override
            public void onFailure(String error) {
                Log.e("Error", error);
            }
        });
    }

    private int getCoordinatorIndex(Coordinator coordinator) {
        if (coordinatorList != null) {
            for (int i = 0; i < coordinatorList.size(); i++) {
                if (coordinatorList.get(i).getId() == coordinator.getId()) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void deleteCourse() {
        final String url = EGradeUtil.URL + "/api/v1/course/delete/" + course.getId();

        HttpUtil.sendRequest(url, Method.DELETE, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response", response);
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Curso deletado com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Erro ao deletar curso! Erro: " + error, Toast.LENGTH_LONG).show();
                Log.e("Error", error);
            }
        });
    }

    private void saveCourse() {
        if (validateInputs()) {
            if (course == null) {
                registerCourse();
            } else {
                updateCourse();
            }
        }
    }

    private boolean validateInputs() {
        if (nameEditText.getText().toString().isEmpty()) {
            nameEditText.setError("Nome é obrigatório");
            return false;
        }
        if (descriptionEditText.getText().toString().isEmpty()) {
            descriptionEditText.setError("Descrição é obrigatória");
            return false;
        }
        return true;
    }

    private void registerCourse() {
        final String url = EGradeUtil.URL + "/api/v1/course/save";
        final String body = HttpUtil.generateRequestBody(
                "name", nameEditText.getText().toString(),
                "description", descriptionEditText.getText().toString(),
                "coordinatorId", String.valueOf(selectedCoordinator.getId())
        );

        HttpUtil.sendRequest(url, Method.POST, body, new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response", response);
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Curso cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Erro ao cadastrar curso! Erro: " + error, Toast.LENGTH_LONG).show();
                Log.e("Error", error);
            }
        });
    }

    private void updateCourse() {
        final String url = EGradeUtil.URL + "/api/v1/course/update";
        final String body = HttpUtil.generateRequestBody(
                "id", String.valueOf(course.getId()),
                "name", nameEditText.getText().toString(),
                "description", descriptionEditText.getText().toString(),
                "coordinatorId", String.valueOf(selectedCoordinator.getId())
        );

        HttpUtil.sendRequest(url, Method.PUT, body, new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response", response);
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Curso atualizado com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Erro ao atualizar curso! Erro: " + error, Toast.LENGTH_LONG).show();
                Log.e("Error", error);
            }
        });
    }

    private void loadCoordinators() {
        final String url = EGradeUtil.URL + "/api/v1/coordinator/findAll";

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response", response);
                final Gson gson = new Gson();
                Type coordinatorListType = new TypeToken<List<Coordinator>>() {}.getType();
                coordinatorList = gson.fromJson(response, coordinatorListType);

                runOnUiThread(() -> {
                    ArrayAdapter<Coordinator> adapter = new ArrayAdapter<>(EditCourseActivity.this, android.R.layout.simple_spinner_item, coordinatorList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    coordinatorSpinner.setAdapter(adapter);
                    if (course != null) {
                        coordinatorSpinner.setSelection(getCoordinatorIndex(course.getCoordinator()));
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Log.e("Error", error);
            }
        });
    }
}