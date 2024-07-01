package com.deopraglabs.egrade.view;

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
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.EGradeUtil;

public class StudentProfileFragment extends Fragment {

    private Student student;

    private TextView id, name, birthDate, course, email, phoneNumber;
    private ImageView profileImage;

    private Button btnEditData;

    public static StudentProfileFragment newInstance(Student student) {
        StudentProfileFragment fragment = new StudentProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", student);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            student = (Student) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_student_profile, container, false);

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
        course = view.findViewById(R.id.course);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        profileImage = view.findViewById(R.id.profileImage);
        btnEditData = view.findViewById(R.id.btnEditData);

        id.setText(String.valueOf(student.getId()));
        name.setText(student.getName());
        birthDate.setText(EGradeUtil.dateToString(student.getBirthDate()));
        course.setText(student.getCourse().getName());
        email.setText(student.getEmail());
        phoneNumber.setText(EGradeUtil.formatNumber(student.getPhoneNumber()));

        if (student.getProfilePicture() != null) {
            profileImage.setImageBitmap(EGradeUtil.base64ToBitmap(student.getProfilePicture()));
        }

        return view;
    }
}