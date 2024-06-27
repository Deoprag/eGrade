package com.deopraglabs.egrade.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Gender;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditCoordinatorActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PERMISSION = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private Student coordinatorEdit;
    private Coordinator coordinator;

    private TextView textId;
    private EditText nameEditText, cpfEditText, emailEditText, phoneEditText, birthDateEditText, passwordEditText;
    private ImageView profileImageView;
    private Button deleteButton, saveButton;
    private CheckBox activeCheckBox;
    private Spinner courseSpinner, genderSpinner;
    private Gender selectedGender;
    private Bitmap selectedPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coordinator);

        Intent intent = getIntent();
        if (intent != null) {
            coordinatorEdit = (Student) intent.getSerializableExtra("coordinatorEdit");
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
        genderSpinner = findViewById(R.id.genderSpinner);
        activeCheckBox = findViewById(R.id.activeCheckBox);
        passwordEditText = findViewById(R.id.passwordEditText);

        setupGenderSpinner();
        requestStoragePermission();

        if (coordinatorEdit != null) {
            textId.setText("Matrícula: " + coordinatorEdit.getId());
            nameEditText.setText(coordinatorEdit.getName());
            cpfEditText.setText(EGradeUtil.formatCpf(coordinatorEdit.getCpf()));
            emailEditText.setText(coordinatorEdit.getEmail());
            phoneEditText.setText(EGradeUtil.formatNumber(coordinatorEdit.getPhoneNumber()));
            birthDateEditText.setText(EGradeUtil.dateToString(coordinatorEdit.getBirthDate()));
            activeCheckBox.setChecked(coordinatorEdit.isActive());
            genderSpinner.setSelection(coordinatorEdit.getGender().equals(Gender.M) ? 0 : coordinatorEdit.getGender().equals(Gender.F) ? 1 : 2);

            if (coordinatorEdit.getProfilePicture() != null) {
                profileImageView.setImageBitmap(EGradeUtil.convertImageFromByte(coordinatorEdit.getProfilePicture().getBytes()));
            }

        } else {
            textId.setVisibility(View.INVISIBLE);
            deleteButton.setEnabled(false);
            deleteButton.setVisibility(View.INVISIBLE);
        }

        deleteButton.setOnClickListener(v -> deleteStudent());

        saveButton.setOnClickListener(v -> saveStudent());

        cpfEditText.addTextChangedListener(new TextWatcher() {

            private boolean isUpdating = false;
            private String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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
            public void afterTextChanged(Editable s) {
            }
        });

        phoneEditText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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
            public void afterTextChanged(Editable s) {
            }
        });

        birthDateEditText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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
            public void afterTextChanged(Editable s) {
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void deleteStudent() {
        final String url = EGradeUtil.URL + "/api/v1/coordinatorEdit/delete/" + coordinatorEdit.getId();

        HttpUtil.sendRequest(url, Method.DELETE, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Estudante deletado com sucesso!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Erro ao deletar estudante! Erro:" + error, Toast.LENGTH_LONG).show();
                Log.e("Erro", error);
            }
        });
    }

    private void saveStudent() {
        if (coordinatorEdit == null) {
            registerStudent();
        } else {
            updateStudent();
        }
    }

    private void registerStudent() {
        final String url = EGradeUtil.URL + "/api/v1/coordinator/save";
        final String body = HttpUtil.generateRequestBody(
                "name", nameEditText.getText().toString(),
                "cpf", cpfEditText.getText().toString().replaceAll("[-.]", ""),
                "gender", selectedGender.toString(),
                "email", emailEditText.getText().toString(),
                "phoneNumber", phoneEditText.getText().toString().replaceAll("[() -]", ""),
                "birthDate", birthDateEditText.getText().toString(),
                "password", passwordEditText.getText().toString(),
                "active", Boolean.toString(activeCheckBox.isChecked()),
                "profilePicture", selectedPhoto != null ? Base64.encodeToString(EGradeUtil.bitmapToByteArray(selectedPhoto), Base64.DEFAULT) : ""
        );

        HttpUtil.sendRequest(url, Method.POST, body, new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Erro ao cadastrar usuário! Erro:" + error, Toast.LENGTH_LONG).show();
                Log.e("Erro", error);
            }
        });
    }

    private void updateStudent() {
        final String url = EGradeUtil.URL + "/api/v1/coordinator/update";
        final String body = HttpUtil.generateRequestBody(
                "id", String.valueOf(coordinatorEdit.getId()),
                "name", nameEditText.getText().toString(),
                "cpf", cpfEditText.getText().toString().replaceAll("[-.]", ""),
                "gender", selectedGender.toString(),
                "email", emailEditText.getText().toString(),
                "phoneNumber", phoneEditText.getText().toString().replaceAll("[() -]", ""),
                "birthDate", birthDateEditText.getText().toString(),
                "password", passwordEditText.getText().toString(),
                "active", Boolean.toString(activeCheckBox.isChecked()),
                "profilePicture", selectedPhoto != null ? Base64.encodeToString(EGradeUtil.bitmapToByteArray(selectedPhoto), Base64.URL_SAFE) : ""
        );

        HttpUtil.sendRequest(url, Method.PUT, body, new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Usuário atualizado com sucesso!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Erro ao atualizar usuário! Erro:" + error, Toast.LENGTH_LONG).show();
                Log.e("Erro", error);
            }
        });
    }

    private void setupGenderSpinner() {
        final Map<Gender, String> genderMap = new HashMap<>();
        genderMap.put(Gender.M, "Masculino");
        genderMap.put(Gender.F, "Feminino");
        genderMap.put(Gender.O, "Outro");

        List<String> genderDescriptions = new ArrayList<>();
        for (Gender gender : Gender.values()) {
            genderDescriptions.add(genderMap.get(gender));
        }

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, genderDescriptions);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = Gender.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                selectedPhoto = bitmap;
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}