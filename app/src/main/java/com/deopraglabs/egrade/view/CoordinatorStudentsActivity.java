package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.content.res.Configuration;
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
import com.deopraglabs.egrade.adapter.StudentAdapter;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Course;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorStudentsActivity extends AppCompatActivity {

    private Coordinator coordinator;

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<Student> studentList;
    private List<Course> courseList;
    private Spinner courseSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_students);

        coordinator = (Coordinator) getIntent().getSerializableExtra("coordinator");

        studentList = new ArrayList<>();
        courseList = new ArrayList<>();

        adapter = new StudentAdapter(this, studentList, new StudentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Student student) {
                Intent intent = new Intent(CoordinatorStudentsActivity.this, EditStudentActivity.class);
                intent.putExtra("student", student);
                intent.putExtra("coordinator", coordinator);
                startActivity(intent);
            }
        });

        loadCourses();

        recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        courseSpinner = findViewById(R.id.courseSpinner);

        Button addStudentButton = findViewById(R.id.addStudentButton);
        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoordinatorStudentsActivity.this, EditStudentActivity.class);
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
                loadStudentsByCourse(selectedCourse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadStudentsByCourse(Course course) {
        final String url = EGradeUtil.URL + "/api/v1/student/findAllByCourse/" + course.getId();

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta Estudantes", response);
                Gson gson = new Gson();
                Type studentListType = new TypeToken<List<Student>>() {}.getType();
                List<Student> students = gson.fromJson(response, studentListType);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (students != null) {
                            studentList.clear();
                            studentList.addAll(students);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("Erro", "Lista de estudantes retornada é nula");
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
