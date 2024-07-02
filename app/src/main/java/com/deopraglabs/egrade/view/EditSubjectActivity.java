package com.deopraglabs.egrade.view;

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

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Professor;
import com.deopraglabs.egrade.model.Subject;
import com.deopraglabs.egrade.util.DataHolder;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EditSubjectActivity extends AppCompatActivity {

    private Subject subject;
    private List<Professor> professorList;
    private Professor selectedProfessor;
    private Coordinator coordinator;

    private TextView textId;
    private EditText nameEditText;
    private Spinner professorSpinner;
    private Button deleteButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);

        if (DataHolder.getInstance().getSubject() != null) {
            subject = DataHolder.getInstance().getSubject();
        }
        coordinator = DataHolder.getInstance().getCoordinator();

        textId = findViewById(R.id.textId);
        nameEditText = findViewById(R.id.nameEditText);
        professorSpinner = findViewById(R.id.professorSpinner);
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);

        loadProfessors();

        deleteButton.setOnClickListener(v -> deleteSubject());

        saveButton.setOnClickListener(v -> saveSubject());
    }

    private void loadProfessors() {
        final String url = EGradeUtil.URL + "/api/v1/professor/findAll";

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                final Gson gson = new Gson();
                Type professorListType = new com.google.gson.reflect.TypeToken<List<Professor>>() {
                }.getType();
                professorList = gson.fromJson(response, professorListType);

                runOnUiThread(() -> {
                    setupProfessorSpinner();
                    if (subject != null) {
                        textId.setText("ID: " + subject.getId());
                        nameEditText.setText(subject.getName());
                        if (subject.getProfessor() != null) {
                            professorSpinner.setSelection(professorList.indexOf(subject.getProfessor()));
                        }
                    } else {
                        textId.setVisibility(View.INVISIBLE);
                        deleteButton.setEnabled(false);
                        deleteButton.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Log.e("Erro", error);
            }
        });
    }

    private void setupProfessorSpinner() {
        if (professorList == null) {
            return;
        }

        List<String> professorNames = new ArrayList<>();
        for (Professor professor : professorList) {
            professorNames.add(professor.getName());
        }

        ArrayAdapter<String> professorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, professorNames);
        professorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professorSpinner.setAdapter(professorAdapter);

        professorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProfessor = professorList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void saveSubject() {
        if (subject == null) {
            registerSubject();
        } else {
            updateSubject();
        }
    }

    private void deleteSubject() {
        final String url = EGradeUtil.URL + "/api/v1/subject/delete/" + subject.getId();

        HttpUtil.sendRequest(url, Method.DELETE, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                runOnUiThread(() -> {
                    Toast.makeText(EditSubjectActivity.this, "Matéria deletada com sucesso!", Toast.LENGTH_LONG).show();
                    synchronized (getParent()) {
                        notifyAll();
                    }
                    DataHolder.getInstance().setCoordinator(coordinator);
                    finish();
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(EditSubjectActivity.this, "Erro ao deletar matéria! Erro:" + error, Toast.LENGTH_LONG).show();
                    Log.e("Erro", error);
                });
            }
        });
    }

    private void registerSubject() {
        final String url = EGradeUtil.URL + "/api/v1/subject/save";
        final String body = HttpUtil.generateRequestBody(
                "name", nameEditText.getText().toString(),
                "professor_id", String.valueOf(selectedProfessor.getId())
        );

        HttpUtil.sendRequest(url, Method.POST, body, new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                runOnUiThread(() -> {
                    Toast.makeText(EditSubjectActivity.this, "Matéria cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                    synchronized (getParent()) {
                        notifyAll();
                    }
                    DataHolder.getInstance().setCoordinator(coordinator);
                    finish();
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(EditSubjectActivity.this, "Erro ao cadastrar matéria! Erro:" + error, Toast.LENGTH_LONG).show();
                    Log.e("Erro", error);
                });
            }
        });
    }

    private void updateSubject() {
        final String url = EGradeUtil.URL + "/api/v1/subject/update";
        final String body = HttpUtil.generateRequestBody(
                "id", String.valueOf(subject.getId()),
                "name", nameEditText.getText().toString(),
                "professor_id", String.valueOf(selectedProfessor.getId())
        );

        HttpUtil.sendRequest(url, Method.PUT, body, new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                runOnUiThread(() -> {
                    Toast.makeText(EditSubjectActivity.this, "Matéria atualizada com sucesso!", Toast.LENGTH_LONG).show();
                    synchronized (getParent()) {
                        notifyAll();
                    }
                    DataHolder.getInstance().setCoordinator(coordinator);
                    finish();
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(EditSubjectActivity.this, "Erro ao atualizar matéria! Erro:" + error, Toast.LENGTH_LONG).show();
                    Log.e("Erro", error);
                });
            }
        });
    }
}
