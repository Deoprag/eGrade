package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.databinding.ActivityCoordinatorBinding;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Student;

public class CoordinatorActivity extends AppCompatActivity {

    ActivityCoordinatorBinding binding;

    private Coordinator coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoordinatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Intent intent = getIntent();
        coordinator = (Coordinator) intent.getSerializableExtra("user");

        replaceFragment(new CoordinatorHomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            if (item.getItemId() == R.id.coordinator_classes) {
                selectedFragment = CoordinatorStudentsFragment.newInstance(coordinator);
            } else if (item.getItemId() == R.id.coordinator_courses) {
                selectedFragment = CoordinatorCourseFragment.newInstance(coordinator);
            } else if (item.getItemId() == R.id.coordinator_profile) {
                selectedFragment = CoordinatorProfileFragment.newInstance(coordinator);
            } else {
                selectedFragment = new CoordinatorHomeFragment();
            }

            replaceFragment(selectedFragment);
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