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
import com.deopraglabs.egrade.util.DataHolder;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorCourseActivity extends AppCompatActivity {

    private static final int EDIT_COURSE_REQUEST = 1;
    private static final int ADD_COURSE_REQUEST = 2;

    private RecyclerView recyclerView;
    private Coordinator coordinator;
    private List<Course> courseList;
    private CourseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_course);

        coordinator = DataHolder.getInstance().getCoordinator();
        courseList = new ArrayList<>();

        adapter = new CourseAdapter(this, courseList, course -> {
            Intent intent = new Intent(CoordinatorCourseActivity.this, EditCourseActivity.class);
            DataHolder.getInstance().setCourse(course);
            DataHolder.getInstance().setCoordinator(coordinator);
            startActivityForResult(intent, EDIT_COURSE_REQUEST);
        });

        recyclerView = findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button addCourseButton = findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(v -> {
            Intent intent = new Intent(CoordinatorCourseActivity.this, EditCourseActivity.class);
            DataHolder.getInstance().setCourse(null);
            DataHolder.getInstance().setCoordinator(coordinator);
            startActivityForResult(intent, ADD_COURSE_REQUEST);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == EDIT_COURSE_REQUEST || requestCode == ADD_COURSE_REQUEST) && resultCode == RESULT_OK) {
            loadCourses();
        }
    }
}
