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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.adapter.StudentAttendanceAdapter;
import com.deopraglabs.egrade.model.Attendance;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Professor;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.model.Subject;
import com.deopraglabs.egrade.util.DataHolder;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class EditAttendanceActivity extends AppCompatActivity {

    private Professor professor;
    private TextView textDate, textSubject;
    private RecyclerView recyclerView;
    private StudentAttendanceAdapter adapter;
    private List<Student> studentList;
    private Subject subject;
    private Button buttonSave;
    private Attendance attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_attendance);

        professor = DataHolder.getInstance().getProfessor();
        attendance = DataHolder.getInstance().getAttendance();
        subject = DataHolder.getInstance().getSubject();

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.background_dark));
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            default:
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(android.R.color.background_light));
                break;
        }

        textDate = findViewById(R.id.textDate);
        textSubject = findViewById(R.id.subjectTitle);
        buttonSave = findViewById(R.id.buttonSave);

        studentList = new ArrayList<>();
        adapter = new StudentAttendanceAdapter(this, studentList);

        recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        textDate.setText("Data: " + EGradeUtil.dateToString(new Date()));

        if (attendance != null) {
            textSubject.setText(attendance.getSubject().getName());
            buttonSave.setVisibility(View.INVISIBLE);
            loadStudentsBySubject(attendance.getSubject());
        } else {
            textSubject.setVisibility(View.INVISIBLE);
            loadStudentsBySubject(subject);
        }

        buttonSave.setOnClickListener(v -> saveAttendance());
    }

    private void loadStudentsBySubject(Subject subject) {
        final String url = EGradeUtil.URL + "/api/v1/student/findAllBySubject/" + subject.getId();

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response Students", response);
                Gson gson = new Gson();
                Type studentListType = new TypeToken<List<Student>>() {}.getType();
                List<Student> students = gson.fromJson(response, studentListType);
                runOnUiThread(() -> {
                    if (students != null) {
                        studentList.clear();
                        studentList.addAll(students);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(EditAttendanceActivity.this, "Nenhum estudante encontrado", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Log.e("Error", error);
                runOnUiThread(() -> Toast.makeText(EditAttendanceActivity.this, "Erro ao carregar estudantes", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void saveAttendance() {
        final String url = EGradeUtil.URL + "/api/v1/attendance/save";
        Set<Student> selectedStudents = adapter.getSelectedStudents();
        for (Student student : studentList) {
            final String body = HttpUtil.generateRequestBody(
                    "date", EGradeUtil.dateToString(new Date()),
                    "isPresent", selectedStudents.contains(student) ? "true" : "false",
                    "studentId", String.valueOf(student.getId()),
                    "subjectId", String.valueOf(subject.getId())
            );

            HttpUtil.sendRequest(url, Method.POST, body, new HttpUtil.HttpRequestListener() {
                @Override
                public void onSuccess(String response) {}

                @Override
                public void onFailure(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Erro ao cadastrar chamada! Erro: " + error, Toast.LENGTH_LONG).show();
                    });
                    Log.e("Error", error);
                }
            });
        }

        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), "Chamada cadastrada com sucesso!", Toast.LENGTH_LONG).show();
            DataHolder.getInstance().setProfessor(professor);
            setResult(RESULT_OK);
            finish();
        });
    }
}
