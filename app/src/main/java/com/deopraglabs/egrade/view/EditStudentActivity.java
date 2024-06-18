package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Course;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EditStudentActivity extends AppCompatActivity {
    private Student student;
    private Coordinator coordinator;

    private TextView textId;
    private EditText nameEditText, cpfEditText, emailEditText, phoneEditText, birthDateEditText;
    private ImageView profileImageView;
    private Button deleteButton, saveButton;
    private Spinner courseSpinner;
    private List<Course> courseList;
    private Course selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        Intent intent = getIntent();
        if (intent != null) {
            student = (Student) intent.getSerializableExtra("student");
            coordinator = (Coordinator) intent.getSerializableExtra("coordinator");
        }

        nameEditText = findViewById(R.id.nameEditText);
        textId = findViewById(R.id.textId);
        cpfEditText = findViewById(R.id.cpfEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        profileImageView = findViewById(R.id.profileImageView);
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);
        courseSpinner = findViewById(R.id.courseSpinner);

        loadCourses();

        if (student != null) {
            textId.setText("Matrícula: " + student.getId());
            nameEditText.setText(student.getName());
            cpfEditText.setText(EGradeUtil.formatCpf(student.getCpf()));
            emailEditText.setText(student.getEmail());
            phoneEditText.setText(EGradeUtil.formatNumber(student.getPhoneNumber()));
            birthDateEditText.setText(EGradeUtil.dateToString(student.getBirthDate()));

            if (student.getProfilePicture() != null) {
                profileImageView.setImageBitmap(EGradeUtil.convertImageFromByte(student.getProfilePicture()));
            }
        } else {
            deleteButton.setEnabled(false);
            deleteButton.setVisibility(View.INVISIBLE);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStudent();
            }
        });

        // Adicionando os TextWatchers para formatação
        cpfEditText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                boolean hasMask = s.toString().indexOf('.') > -1 || s.toString().indexOf('-') > -1;
                String str = s.toString().replaceAll("[.]", "").replaceAll("[-]", "");

                if (count > before) {
                    if (str.length() > 11) {
                        str = str.substring(0, 3) + '.' + str.substring(3, 6) + '.' + str.substring(6, 9) + '-' + str.substring(9, str.length() - 1);
                    } else if (str.length() > 9) {
                        str = str.substring(0, 3) + '.' + str.substring(3, 6) + '.' + str.substring(6, 9) + '-' + str.substring(9);
                    } else if (str.length() > 6) {
                        str = str.substring(0, 3) + '.' + str.substring(3, 6) + '.' + str.substring(6);
                    } else if (str.length() > 3) {
                        str = str.substring(0, 3) + '.' + str.substring(3);
                    }
                    isUpdating = true;
                    cpfEditText.setText(str);
                    cpfEditText.setSelection(cpfEditText.getText().length());
                } else {
                    old = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        phoneEditText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String str = s.toString().replaceAll("[()]", "").replaceAll("[-]", "").replaceAll("[ ]", "");

                if (count > before) {
                    if (str.length() > 11) {
                        str = '(' + str.substring(0, 2) + ") " + str.substring(2, 7) + '-' + str.substring(7, str.length() - 1);
                    } else if (str.length() > 10) {
                        str = '(' + str.substring(0, 2) + ") " + str.substring(2, 7) + '-' + str.substring(7);
                    } else if (str.length() > 6) {
                        str = '(' + str.substring(0, 2) + ") " + str.substring(2, 6) + '-' + str.substring(6);
                    } else if (str.length() > 2) {
                        str = '(' + str.substring(0, 2) + ") " + str.substring(2);
                    }
                    isUpdating = true;
                    phoneEditText.setText(str);
                    phoneEditText.setSelection(phoneEditText.getText().length());
                } else {
                    old = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        birthDateEditText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String str = s.toString().replaceAll("[/]", "");

                if (count > before) {
                    if (str.length() > 8) {
                        str = str.substring(0, 2) + '/' + str.substring(2, 4) + '/' + str.substring(4, str.length() - 1);
                    } else if (str.length() > 4) {
                        str = str.substring(0, 2) + '/' + str.substring(2, 4) + '/' + str.substring(4);
                    } else if (str.length() > 2) {
                        str = str.substring(0, 2) + '/' + str.substring(2);
                    }
                    isUpdating = true;
                    birthDateEditText.setText(str);
                    birthDateEditText.setSelection(birthDateEditText.getText().length());
                } else {
                    old = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void deleteStudent() {
        // Implement delete logic here
        finish();
    }

    private void saveStudent() {
        // Implement save logic here
    }

    private void loadCourses() {
        final String url = EGradeUtil.URL + "/api/v1/course/findByCoordinatorId/" + coordinator.getId();

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                final Gson gson = new Gson();
                Type courseListType = new com.google.gson.reflect.TypeToken<List<Course>>(){}.getType();
                courseList = gson.fromJson(response, courseListType);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupCourseSpinner();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Log.e("Erro", error);
            }
        });
    }

    private void setupCourseSpinner() {
        if (courseList == null) {
            return;
        }

        List<String> courseNames = new ArrayList<>();
        for (Course course : courseList) {
            courseNames.add(course.getName());
        }

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courseNames);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourse = courseList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
