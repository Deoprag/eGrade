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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.adapter.AttendanceAdapter;
import com.deopraglabs.egrade.model.Attendance;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Professor;
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

public class ProfessorAttendanceFragment extends Fragment {

    private static final int EDIT_ATTENDANCE_REQUEST = 1;
    private static final int ADD_ATTENDANCE_REQUEST = 2;

    private RecyclerView recyclerView;
    private Professor professor;
    private AttendanceAdapter adapter;
    private List<Attendance> attendanceList;
    private List<Subject> subjectList;
    private Spinner subjectSpinner;
    private Subject selectedSubject;
    private List<Date> attendanceDates = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_professor_attendance, container, false);

        professor = DataHolder.getInstance().getProfessor();

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

        attendanceList = new ArrayList<>();
        subjectList = new ArrayList<>();
        adapter = new AttendanceAdapter(getContext(), attendanceList, attendance -> {
            Intent intent = new Intent(getActivity(), EditAttendanceActivity.class);
            DataHolder.getInstance().setAttendance(attendance);
            DataHolder.getInstance().setSubject(selectedSubject);
            startActivityForResult(intent, EDIT_ATTENDANCE_REQUEST);
        });

        subjectSpinner = view.findViewById(R.id.subjectSpinner);

        loadSubjects();

        recyclerView = view.findViewById(R.id.recyclerViewAttendances);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        Button addAttendanceButton = view.findViewById(R.id.addAttendanceButton);
        addAttendanceButton.setOnClickListener(v -> {
            loadEditAttendance();
        });

        return view;
    }

    private void loadEditAttendance() {
        boolean attendanceAlreadyTakenToday = false;
        for (Attendance attendance : attendanceList) {
            if (EGradeUtil.isSameDate(new Date(), attendance.getDate())) {
                attendanceAlreadyTakenToday = true;
                break;
            }
        }

        if (!attendanceAlreadyTakenToday) {
            Intent intent = new Intent(getActivity(), EditAttendanceActivity.class);
            DataHolder.getInstance().setAttendance(null);
            DataHolder.getInstance().setSubject(selectedSubject);
            startActivityForResult(intent, ADD_ATTENDANCE_REQUEST);
        } else {
            Toast.makeText(getContext(), "Chamada já realizada na data de hoje!", Toast.LENGTH_SHORT).show();
        }
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
                loadAttendancesBySubject(selectedSubject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void loadAttendancesBySubject(Subject subject) {
        final String url = EGradeUtil.URL + "/api/v1/attendance/findBySubjectId/" + subject.getId();

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta Presença", response);
                Gson gson = new Gson();
                Type attendanceListType = new TypeToken<List<Attendance>>() {}.getType();
                List<Attendance> attendances = gson.fromJson(response, attendanceListType);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (attendances != null) {
                            attendanceList.clear();
                            for (Attendance attendance: attendances) {
                                if (!attendanceDates.contains(attendance.getDate())) {
                                    attendanceDates.add(attendance.getDate());
                                    attendanceList.add(attendance);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("Erro", "Lista de presenças retornada é nula");
                        }
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                Log.e("Erro", error);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == EDIT_ATTENDANCE_REQUEST || requestCode == ADD_ATTENDANCE_REQUEST) && resultCode == getActivity().RESULT_OK) {
            loadSubjects();
        }
    }
}