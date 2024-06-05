package com.deopraglabs.egrade.view;

import android.content.Intent;
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

        id = view.findViewById(R.id.id);
        birthDate = view.findViewById(R.id.birthDate);
        course = view.findViewById(R.id.course);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        profileImage = view.findViewById(R.id.profileImage);
        btnEditData = view.findViewById(R.id.btnEditData);

        id.setText("Matrícula: " + student.getId());
        birthDate.setText("Nascimento: " + EGradeUtil.dateToString(student.getBirthDate()));
        course.setText("Curso: " + student.getCourse().getName());
        email.setText("Email: " + student.getEmail());
        phoneNumber.setText("Celular " + EGradeUtil.formatNumber(student.getPhoneNumber()));
        if (student.getProfilePicture() != null) {
            profileImage.setImageBitmap(EGradeUtil.convertImageFromByte(student.getProfilePicture()));
        } else {
            profileImage.setImageResource(R.drawable.profile_icon);
        }

        return view;
    }
}