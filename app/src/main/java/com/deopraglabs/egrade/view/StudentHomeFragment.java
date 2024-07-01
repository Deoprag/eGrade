package com.deopraglabs.egrade.view;// StudentHomeFragment.java

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.adapter.RankingAdapter;
import com.deopraglabs.egrade.model.Grade;
import com.deopraglabs.egrade.model.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentHomeFragment extends Fragment {

    private RecyclerView rankingRecyclerView;
    private RankingAdapter rankingAdapter;
    private List<Grade> gradesList = new ArrayList<>();
    private List<Subject> subjectsList = new ArrayList<>();
    private Spinner subjectSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        rankingRecyclerView = view.findViewById(R.id.rankingRecyclerView);
        subjectSpinner = view.findViewById(R.id.subjectSpinner);

        populateSubjects();
        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subjectsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Subject subject = (Subject) parent.getItemAtPosition(position);
                updateRanking(subject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        rankingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rankingAdapter = new RankingAdapter(new ArrayList<>());
        rankingRecyclerView.setAdapter(rankingAdapter);

        updateRanking(null);

        return view;
    }

    private void populateSubjects() {
        //adicionar no subjectList
    }

    private void updateRanking(@Nullable Subject subject) {
        List<Grade> filteredGrades = filterGradesBySubject(subject);
        rankingAdapter.updateGrades(filteredGrades);
    }

    private List<Grade> filterGradesBySubject(@Nullable Subject subject) {
        List<Grade> allGrades = getAllGrades();

        if (subject == null || subject.getName().equals("Todas")) {
            return allGrades.stream()
                    .sorted((g1, g2) -> Float.compare(g2.getN1(), g1.getN1())) // Ordena por nota, por exemplo
                    .limit(5)
                    .collect(Collectors.toList());
        }

        return allGrades.stream()
                .filter(grade -> grade.getSubject().getName().equals(subject.getName()))
                .sorted((g1, g2) -> Float.compare(g2.getN1(), g1.getN1()))
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<Grade> getAllGrades() {
        // Listar grades
        return null;
    }
}
