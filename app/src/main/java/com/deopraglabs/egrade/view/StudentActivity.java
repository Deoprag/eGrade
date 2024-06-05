package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.databinding.ActivityStudentBinding;
import com.deopraglabs.egrade.model.Student;

public class StudentActivity extends AppCompatActivity {

    ActivityStudentBinding binding;

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("user");

        replaceFragment(new StudentHomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            if (item.getItemId() == R.id.teachers) {
                selectedFragment = new StudentProfessorFragment();
            } else if (item.getItemId() == R.id.grades) {
                selectedFragment = new StudentGradeFragment();
            } else if (item.getItemId() == R.id.profile) {
                selectedFragment = new StudentProfileFragment();
            } else {
                selectedFragment = new StudentHomeFragment();
            }

            replaceFragment(selectedFragment);
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        Bundle args = new Bundle();
        args.putSerializable("user", student);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}