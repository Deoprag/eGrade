package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Professor;
import com.deopraglabs.egrade.util.DataHolder;
import com.deopraglabs.egrade.util.EGradeUtil;

import java.util.Objects;

public class ProfessorProfileFragment extends Fragment {

    private Professor professor;

    private TextView id, name, birthDate, email, phoneNumber;
    private ImageView profileImage;

    private Button btnEditData, btnSignOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        professor = DataHolder.getInstance().getProfessor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_professor_profile, container, false);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                view.setBackgroundColor(getResources().getColor(R.color.background_dark));
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            default:
                view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                break;
        }

        id = view.findViewById(R.id.id);
        name = view.findViewById(R.id.name);
        birthDate = view.findViewById(R.id.birthDate);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        profileImage = view.findViewById(R.id.profileImage);
        btnSignOut = view.findViewById(R.id.btnSignOut);

        id.setText(String.valueOf(professor.getId()));
        name.setText(professor.getName());
        birthDate.setText(EGradeUtil.dateToString(professor.getBirthDate()));
        email.setText(professor.getEmail());
        phoneNumber.setText(EGradeUtil.formatNumber(professor.getPhoneNumber()));

        if (professor.getProfilePicture() != null) {
            profileImage.setImageBitmap(EGradeUtil.base64ToBitmap(professor.getProfilePicture()));
        }

        btnSignOut.setOnClickListener((v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        }));

        return view;
    }
}