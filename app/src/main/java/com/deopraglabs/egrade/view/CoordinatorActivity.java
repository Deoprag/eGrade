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
import com.deopraglabs.egrade.util.DataHolder;

public class CoordinatorActivity extends AppCompatActivity {

    ActivityCoordinatorBinding binding;

    private Coordinator coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoordinatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.bottomAppBar);

        coordinator = DataHolder.getInstance().getCoordinator();

        replaceFragment(new CoordinatorHomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            DataHolder.getInstance().setCoordinator(coordinator);
            if (item.getItemId() == R.id.coordinator_register_people) {
                selectedFragment = new CoordinatorRegisterPeopleFragment();
            } else if (item.getItemId() == R.id.coordinator_profile) {
                selectedFragment = new CoordinatorProfileFragment();
            } else if (item.getItemId() == R.id.coordinator_register_other) {
                selectedFragment = new CoordinatorRegisterOtherFragment();
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