package com.deopraglabs.egrade.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Professor;
import com.deopraglabs.egrade.util.DataHolder;

public class ProfessorSolicitationFragment extends Fragment {

    private Professor professor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        professor = DataHolder.getInstance().getProfessor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_solicitation, container, false);
    }
}