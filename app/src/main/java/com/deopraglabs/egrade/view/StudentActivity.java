package com.deopraglabs.egrade.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.databinding.ActivityStudentBinding;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.DataHolder;

public class StudentActivity extends AppCompatActivity {

    ActivityStudentBinding binding;

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        student = DataHolder.getInstance().getStudent();

        replaceFragment(new RankingFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            DataHolder.getInstance().setStudent(student);

            if (item.getItemId() == R.id.student_solicitations) {
                selectedFragment = new StudentSolicitationsFragment();
            } else if (item.getItemId() == R.id.student_grades) {
                selectedFragment = new StudentGradeFragment();
            } else if (item.getItemId() == R.id.student_profile) {
                selectedFragment = new StudentProfileFragment();
            } else {
                selectedFragment = new RankingFragment();
            }

            replaceFragment(selectedFragment);
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        DataHolder.setStudent(student);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}