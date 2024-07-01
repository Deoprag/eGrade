package com.deopraglabs.egrade.view;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Grade;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.model.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class StudentGradeFragment extends Fragment {

    private Student student;

    private ListView gradeList;

    private ArrayAdapter<Grade> adapter;

    public static StudentProfileFragment newInstance(Student student) {
        StudentProfileFragment fragment = new StudentProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", student);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            student = (Student) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_grade, container, false);

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

        gradeList = view.findViewById(R.id.gradeList);

        if (student.getGrades() != null) {
            final List<Grade> grades = student.getGrades();

            adapter = new ArrayAdapter<Grade>(getContext(), R.layout.item_grade, R.id.textViewSubject, grades) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    final TextView textViewSubject = view.findViewById(R.id.textViewSubject);
                    final TextView textViewProfessor = view.findViewById(R.id.textViewProfessor);
                    final TextView textViewAbscence = view.findViewById(R.id.textViewAbsence);
                    final TextView textViewGrade = view.findViewById(R.id.textViewGrade);

                    final Grade grade = grades.get(position);
                    textViewSubject.setText(grade.getSubject().getName());
                    textViewProfessor.setText("Professor: " + grade.getSubject().getProfessor().getName());
                    textViewAbscence.setText("Faltas: 4");
                    textViewGrade.setText(String.format("Nota: %.1f", (grade.getN1() + grade.getN2()) / 2));

                    return view;
                }
            };
        }

        gradeList.setAdapter(adapter);

        return view;
    }
}