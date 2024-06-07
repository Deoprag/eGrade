package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.databinding.ActivityProfessorBinding;
import com.deopraglabs.egrade.model.Professor;
import com.deopraglabs.egrade.model.Student;

public class ProfessorActivity extends AppCompatActivity {

    ActivityProfessorBinding binding;

    private Professor professor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfessorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Intent intent = getIntent();
        professor = (Professor) intent.getSerializableExtra("user");

        replaceFragment(new ProfessorHomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.professor_courses) {
                replaceFragment(new ProfessorCoursesFragment());
            } else if (item.getItemId() == R.id.professor_solicitations) {
                replaceFragment(new ProfessorSolicitationFragment());
            } else if (item.getItemId() == R.id.professor_profile) {
                replaceFragment(new ProfessorProfileFragment());
            } else {
                replaceFragment(new ProfessorHomeFragment());
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