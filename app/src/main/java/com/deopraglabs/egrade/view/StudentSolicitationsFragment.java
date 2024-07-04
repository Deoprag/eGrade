package com.deopraglabs.egrade.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.DataHolder;

public class StudentSolicitationsFragment extends Fragment {

    private Student student;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        student = DataHolder.getInstance().getStudent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_solicitation, container, false);
    }
}