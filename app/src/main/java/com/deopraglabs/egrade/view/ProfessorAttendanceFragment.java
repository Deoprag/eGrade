package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.adapter.StudentAttendanceAdapter;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ProfessorAttendanceFragment extends Fragment {

    private Professor professor;
    private TextView textDate;
    private RecyclerView recyclerView;
    private StudentAttendanceAdapter adapter;
    private List<Student> studentList;
    private List<Subject> subjectList;
    private Spinner subjectSpinner;
    private Subject selectedSubject;
    private Button buttonSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        professor = DataHolder.getInstance().getProfessor();
        loadSubjects();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_professor_attendance, container, false);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                view.setBackgroundColor(getResources().getColor(R.color.background_dark));
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            default:
                view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                break;
        }

        textDate = view.findViewById(R.id.textDate);
        textDate.setText("Data: " + EGradeUtil.dateToString(new Date()));

        studentList = new ArrayList<>();
        subjectList = new ArrayList<>();
        adapter = new StudentAttendanceAdapter(getContext(), studentList);

        recyclerView = view.findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        subjectSpinner = view.findViewById(R.id.subjectSpinner);
        buttonSave = view.findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(v -> saveAttendance());

        return view;
    }

    private void loadSubjects() {
        final String url = EGradeUtil.URL + "/api/v1/subject/findByProfessorId/" + professor.getId();

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Response", response);
                Gson gson = new Gson();
                Type subjectListType = new TypeToken<List<Subject>>() {}.getType();
                subjectList = gson.fromJson(response, subjectListType);

                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        if (subjectList != null && !subjectList.isEmpty()) {
                            setupSubjectSpinner();
                        } else {
                            Toast.makeText(getContext(), "Nenhuma matéria encontrada", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                Log.e("Error", error);
                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Erro ao carregar matérias", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void setupSubjectSpinner() {
        if (subjectList == null || subjectList.isEmpty()) {
            return;
        }

        List<String> subjectNames = new ArrayList<>();
        for (Subject subject : subjectList) {
            subjectNames.add(subject.getName());
        }

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, subjectNames);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = subjectList.get(position);
                loadStudentsBySubject(selectedSubject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        if (students != null) {
                            studentList.clear();
                            studentList.addAll(students);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Nenhum estudante encontrado", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                Log.e("Error", error);
                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Erro ao carregar estudantes", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void saveAttendance() {
        Set<Student> selectedStudents = adapter.getSelectedStudents();
        if (!selectedStudents.isEmpty()) {
            Toast.makeText(getContext(), "Presença salva com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Nenhum aluno selecionado.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == -1) {
                loadSubjects();
            }
        }
    }
}
