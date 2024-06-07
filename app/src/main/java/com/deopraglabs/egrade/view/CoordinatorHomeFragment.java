package com.deopraglabs.egrade.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Coordinator;

public class CoordinatorHomeFragment extends Fragment {

    private Coordinator coordinator;

    public static CoordinatorHomeFragment newInstance(Coordinator coordinator) {
        CoordinatorHomeFragment fragment = new CoordinatorHomeFragment();
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
        return inflater.inflate(R.layout.fragment_coordinator_home, container, false);
    }
}