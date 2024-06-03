package com.deopraglabs.egrade.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.databinding.ActivityCoordinatorBinding;

public class CoordinatorActivity extends AppCompatActivity {

    ActivityCoordinatorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoordinatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new StudentHomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.teachers) {
                replaceFragment(new StudentProfessorFragment());
            } else if (item.getItemId() == R.id.grades) {
                replaceFragment(new StudentGradeFragment());
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new StudentProfileFragment());
            } else {
                replaceFragment(new StudentHomeFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}