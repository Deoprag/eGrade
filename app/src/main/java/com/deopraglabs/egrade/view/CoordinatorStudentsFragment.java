package com.deopraglabs.egrade.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Coordinator;

public class CoordinatorStudentsFragment extends Fragment {

    private Coordinator coordinator;

    private CoordinatorActivity ca;

    public static CoordinatorStudentsFragment newInstance(Coordinator coordinator) {
        CoordinatorStudentsFragment fragment = new CoordinatorStudentsFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", coordinator);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coordinator = (Coordinator) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coordinator_students, container, false);
    }
}