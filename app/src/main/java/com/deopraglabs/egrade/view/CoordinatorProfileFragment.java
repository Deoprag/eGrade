package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.util.EGradeUtil;

public class CoordinatorProfileFragment extends Fragment {

    private Coordinator coordinator;

    private TextView id;
    private TextView birthDate;
    private TextView email;
    private TextView phoneNumber;
    private ImageView profileImage;

    private Button btnEditData;

    public static CoordinatorProfileFragment newInstance(Coordinator coordinator) {
        CoordinatorProfileFragment fragment = new CoordinatorProfileFragment();
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
        final View view = inflater.inflate(R.layout.fragment_coordinator_profile, container, false);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                view.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            default:
                view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                break;
        }

        id = view.findViewById(R.id.id);
        birthDate = view.findViewById(R.id.birthDate);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        profileImage = view.findViewById(R.id.profileImage);
        btnEditData = view.findViewById(R.id.btnEditData);

        id.setText(String.valueOf(coordinator.getId()));
        birthDate.setText(EGradeUtil.dateToString(coordinator.getBirthDate()));
        email.setText(coordinator.getEmail());
        phoneNumber.setText(EGradeUtil.formatNumber(coordinator.getPhoneNumber()));

        if (coordinator.getProfilePicture() != null) {
            profileImage.setImageBitmap(EGradeUtil.convertImageFromByte(coordinator.getProfilePicture()));
        }

        return view;
    }
}