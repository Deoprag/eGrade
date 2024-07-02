package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.deopraglabs.egrade.util.DataHolder;
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

        coordinator = DataHolder.getInstance().getCoordinator();

        professorList = new ArrayList<>();
        courseList = new ArrayList<>();

        adapter = new ProfessorAdapter(this, professorList, professor -> {
            Intent intent = new Intent(CoordinatorProfessorsActivity.this, EditProfessorActivity.class);
            DataHolder.getInstance().setProfessor(professor);
            DataHolder.getInstance().setCoordinator(coordinator);
            startActivity(intent);
        });

        loadProfessors();

        recyclerView = findViewById(R.id.recyclerViewProfessors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button addProfessorButton = findViewById(R.id.addProfessorButton);
        addProfessorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoordinatorProfessorsActivity.this, EditProfessorActivity.class);
                DataHolder.getInstance().setProfessor(null);
                DataHolder.getInstance().setCoordinator(coordinator);
                startActivity(intent);
            }
        });

    }

    private void loadProfessors() {
        final String url = EGradeUtil.URL + "/api/v1/professor/findAllByCoordinator/" + DataHolder.getInstance().getCoordinator().getId();

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta Professores", response);
                Gson gson = new Gson();
                Type professorListType = new TypeToken<List<Professor>>() {}.getType();
                List<Professor> professors = gson.fromJson(response, professorListType);

                runOnUiThread(() -> {
                    if (professors != null) {
                        professorList.clear();
                        professorList.addAll(professors);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Erro", "Lista de professores retornada é nula");
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
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadProfessors();
        }
    }
}
