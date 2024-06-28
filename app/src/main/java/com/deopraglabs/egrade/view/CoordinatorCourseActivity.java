package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.adapter.CourseAdapter;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Course;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorCourseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Coordinator coordinator;
    private List<Course> courseList;
    private CourseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_course);

        coordinator = (Coordinator) getIntent().getSerializableExtra("user");
        courseList = new ArrayList<>();

        adapter = new CourseAdapter(this, courseList, course -> {
            // Handle click event if needed
            Intent intent = new Intent(CoordinatorCourseActivity.this, EditCourseActivity.class);
            intent.putExtra("course", course);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button addCourseButton = findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(v -> {
            Intent intent = new Intent(CoordinatorCourseActivity.this, EditCourseActivity.class);
            intent.putExtra("coordinator", coordinator);
            startActivity(intent);
        });

        loadCourses();
    }

    private void loadCourses() {
        final String url = EGradeUtil.URL + "/api/v1/course/findAll";

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                Gson gson = new Gson();
                Type courseListType = new TypeToken<List<Course>>() {}.getType();
                courseList = gson.fromJson(response, courseListType);

                runOnUiThread(() -> {
                    if (courseList != null) {
                        adapter.setCourseList(courseList);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Erro", "Lista de cursos retornada é nula");
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
