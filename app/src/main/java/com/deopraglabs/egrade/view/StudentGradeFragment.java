package com.deopraglabs.egrade.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Grade;
import com.deopraglabs.egrade.model.Student;

import java.util.ArrayList;
import java.util.List;

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

        gradeList = view.findViewById(R.id.gradeList);

//        List<Grade> grades = student.getGrades();
        List<Grade> grades = new ArrayList<>();

        adapter = new ArrayAdapter<Grade>(getContext(), R.layout.item_grade, R.id.textViewSubject, grades) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                final TextView textViewSubject = view.findViewById(R.id.textViewSubject);
                final TextView textViewAbscence = view.findViewById(R.id.textViewAbsence);
                final TextView textViewGradeN1 = view.findViewById(R.id.textViewGradeN1);
                final TextView textViewGradeN2 = view.findViewById(R.id.textViewGradeN2);
                final TextView textViewGrade = view.findViewById(R.id.textViewGradeFinal);

//                Grade grade = grades.get(position);
                textViewSubject.setText("Teste");
                textViewAbscence.setText("10");
//                textViewGradeN1.setText(String.valueOf(grade.getN1()));
//                textViewGradeN2.setText(String.valueOf(grade.getN2()));
//                textViewGrade.setText(String.valueOf((grade.getN1() + grade.getN2()) / 2));

                return view;
            }
        };

        gradeList.setAdapter(adapter);

        return view;
    }
}