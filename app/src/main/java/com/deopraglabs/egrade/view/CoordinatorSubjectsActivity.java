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
import com.deopraglabs.egrade.adapter.SubjectAdapter;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Course;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Subject;
import com.deopraglabs.egrade.util.DataHolder;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_subjects);

        coordinator = DataHolder.getInstance().getCoordinator();

        subjectList = new ArrayList<>();
        courseList = new ArrayList<>();

        adapter = new SubjectAdapter(this, subjectList, subject -> {
            Intent intent = new Intent(CoordinatorSubjectsActivity.this, EditSubjectActivity.class);
            DataHolder.getInstance().setSubject(subject);
            DataHolder.getInstance().setCoordinator(coordinator);
            startActivity(intent);
        });

        loadSubjects();

        recyclerView = findViewById(R.id.recyclerViewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button addSubjectButton = findViewById(R.id.addSubjectButton);
        addSubjectButton.setOnClickListener(v -> {
            Intent intent = new Intent(CoordinatorSubjectsActivity.this, EditSubjectActivity.class);
            DataHolder.getInstance().setSubject(null);
            DataHolder.getInstance().setCoordinator(coordinator);
            startActivity(intent);
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
