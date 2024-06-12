package com.deopraglabs.egrade.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Student;

public class EditStudentActivity extends AppCompatActivity {
    private Student student;
    private EditText nameEditText, cpfEditText, emailEditText, phoneEditText;
    private ImageView profileImageView;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        student = (Student) getIntent().getSerializableExtra("student");

        nameEditText = findViewById(R.id.nameEditText);
        cpfEditText = findViewById(R.id.cpfEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        profileImageView = findViewById(R.id.profileImageView);
        deleteButton = findViewById(R.id.deleteButton);

        if (student != null) {
            nameEditText.setText(student.getName());
            cpfEditText.setText(student.getCpf());
            emailEditText.setText(student.getEmail());
            phoneEditText.setText(student.getPhoneNumber());
            // Load profile picture
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent();
            }
        });

        // Add logic to save the changes
    }

    private void deleteStudent() {
        // Implement the logic to delete the student
        finish();
    }

    // Method to save the changes
}
