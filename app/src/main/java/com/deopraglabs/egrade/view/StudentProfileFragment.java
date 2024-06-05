package com.deopraglabs.egrade.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.EGradeUtil;

public class StudentProfileFragment extends Fragment {

    private Student student;

    private TextView id;
    private TextView birthDate;
    private TextView course;
    private TextView email;
    private TextView phoneNumber;
    private ImageView profileImage;

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

        id = view.findViewById(R.id.id);
        birthDate = view.findViewById(R.id.birthDate);
        course = view.findViewById(R.id.course);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        profileImage = view.findViewById(R.id.profileImage);

        id.setText(String.valueOf(student.getId()));
        birthDate.setText(EGradeUtil.dateToString(student.getBirthDate()));
        course.setText(student.getCourse().getName());
        email.setText(student.getEmail());
        phoneNumber.setText(student.getPhoneNumber());
        if (student.getProfilePicture() != null) {
            profileImage.setImageBitmap(EGradeUtil.convertImageFromByte(student.getProfilePicture()));
        } else {
            profileImage.setImageResource(R.drawable.icon);
        }

        return view;
    }
}