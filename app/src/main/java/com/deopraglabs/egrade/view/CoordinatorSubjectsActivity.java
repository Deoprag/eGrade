package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.adapter.SubjectAdapter;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Subject;
import com.deopraglabs.egrade.model.Course;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorSubjectsActivity extends AppCompatActivity {

    private Coordinator coordinator;
    private RecyclerView recyclerView;
    private SubjectAdapter adapter;
    private List<Subject> subjectList;
    private List<Course> courseList;
    private Spinner courseSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_subjects);

        coordinator = (Coordinator) getIntent().getSerializableExtra("user");

        subjectList = new ArrayList<>();
        courseList = new ArrayList<>();

        adapter = new SubjectAdapter(this, subjectList, subject -> {
            Intent intent = new Intent(CoordinatorSubjectsActivity.this, EditSubjectActivity.class);
            intent.putExtra("subject", subject);
            intent.putExtra("coordinator", coordinator);
            startActivity(intent);
        });

        loadCourses();
        loadSubjects();

        recyclerView = findViewById(R.id.recyclerViewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        courseSpinner = findViewById(R.id.courseSpinner);

        Button addSubjectButton = findViewById(R.id.addSubjectButton);
        addSubjectButton.setOnClickListener(v -> {
            Intent intent = new Intent(CoordinatorSubjectsActivity.this, EditSubjectActivity.class);
            intent.putExtra("coordinator", coordinator);
            startActivity(intent);
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

                runOnUiThread(() -> setupCourseSpinner());
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadSubjects() {
        final String url = EGradeUtil.URL + "/api/v1/subject/findAll/";

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta Matéria", response);
                Gson gson = new Gson();
                Type subjectListType = new TypeToken<List<Subject>>() {}.getType();
                List<Subject> subjects = gson.fromJson(response, subjectListType);

                runOnUiThread(() -> {
                    if (subjects != null) {
                        subjectList.clear();
                        subjectList.addAll(subjects);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Erro", "Lista de matérias retornada é nula");
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
