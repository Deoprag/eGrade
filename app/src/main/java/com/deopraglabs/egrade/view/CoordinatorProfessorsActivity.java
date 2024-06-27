package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.adapter.ProfessorAdapter;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Course;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Professor;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorProfessorsActivity extends AppCompatActivity {

    private Coordinator coordinator;
    private RecyclerView recyclerView;
    private ProfessorAdapter adapter;
    private List<Professor> professorList;
    private List<Course> courseList;
    private Spinner courseSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_professors);

        coordinator = (Coordinator) getIntent().getSerializableExtra("coordinator");

        professorList = new ArrayList<>();
        courseList = new ArrayList<>();

        adapter = new ProfessorAdapter(this, professorList, new ProfessorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Professor professor) {
                Intent intent = new Intent(CoordinatorProfessorsActivity.this, EditProfessorActivity.class);
                intent.putExtra("professor", professor);
                intent.putExtra("coordinator", coordinator);
                startActivity(intent);
            }
        });

        loadCourses();

        recyclerView = findViewById(R.id.recyclerViewProfessors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        courseSpinner = findViewById(R.id.courseSpinner);

        Button addProfessorButton = findViewById(R.id.addProfessorButton);
        addProfessorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoordinatorProfessorsActivity.this, EditProfessorActivity.class);
                intent.putExtra("coordinator", coordinator);
                startActivity(intent);
            }
        });

        setupCourseSpinner();
    }

    private void loadCourses() {
        final String url = EGradeUtil.URL + "/api/v1/course/findByCoordinatorId/" + coordinator.getId();

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                Gson gson = new Gson();
                Type courseListType = new TypeToken<List<Course>>() {}.getType();
                courseList = gson.fromJson(response, courseListType);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupCourseSpinner();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Log.e("Erro", error);
            }
        });
    }

    private void setupCourseSpinner() {
        if (courseList == null) {
            return;
        }

        List<String> courseNames = new ArrayList<>();
        for (Course course : courseList) {
            courseNames.add(course.getName());
        }

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courseNames);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Course selectedCourse = courseList.get(position);
                loadProfessorsByCourse(selectedCourse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadProfessorsByCourse(Course course) {
        final String url = EGradeUtil.URL + "/api/v1/professor/findAllByCourse/" + course.getId();

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta Professores", response);
                Gson gson = new Gson();
                Type professorListType = new TypeToken<List<Professor>>() {}.getType();
                List<Professor> professors = gson.fromJson(response, professorListType);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (professors != null) {
                            professorList.clear();
                            professorList.addAll(professors);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("Erro", "Lista de professores retornada é nula");
                        }
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Log.e("Erro", error);
            }
        });
    }
}
