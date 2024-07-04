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
import com.deopraglabs.egrade.model.Professor;
import com.deopraglabs.egrade.model.Subject;
import com.deopraglabs.egrade.util.DataHolder;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EditProfessorActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PERMISSION = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private Professor professor;

    private Coordinator coordinator;
    private TextView textId;
    private EditText nameEditText, cpfEditText, emailEditText, phoneEditText, birthDateEditText, passwordEditText;
    private ImageView profileImageView;
    private Button deleteButton, saveButton;
    private CheckBox activeCheckBox;
    private Spinner genderSpinner, subjectSpinner;
    private Gender selectedGender;
    private Bitmap selectedPhoto;
    private List<Subject> subjectList;
    private Subject selectedSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_professor);

        if (DataHolder.getInstance().getProfessor() != null) {
            professor = DataHolder.getInstance().getProfessor();
        }
        coordinator = DataHolder.getInstance().getCoordinator();

        nameEditText = findViewById(R.id.nameEditText);
        textId = findViewById(R.id.textId);
        cpfEditText = findViewById(R.id.cpfEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        profileImageView = findViewById(R.id.profileImageView);
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);
        activeCheckBox = findViewById(R.id.activeCheckBox);
        genderSpinner = findViewById(R.id.genderSpinner);
        subjectSpinner = findViewById(R.id.subjectSpinner);

        List<String> genderOptions = new ArrayList<>();
        genderOptions.add("Feminino");
        genderOptions.add("Masculino");
        genderOptions.add("Outro");

        requestStoragePermission();
        loadSubjects();

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderOptions);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        if (professor != null) {
            textId.setText("Matrícula: " + professor.getId());
            nameEditText.setText(professor.getName());
            cpfEditText.setText(EGradeUtil.formatCpf(professor.getCpf()));
            emailEditText.setText(professor.getEmail());
            phoneEditText.setText(EGradeUtil.formatNumber(professor.getPhoneNumber()));
            birthDateEditText.setText(EGradeUtil.dateToString(professor.getBirthDate()));
            activeCheckBox.setChecked(professor.isActive());
            genderSpinner.setSelection(genderAdapter.getPosition(professor.getGender().toString()));
            if (professor.getProfilePicture() != null) profileImageView.setImageBitmap(EGradeUtil.base64ToBitmap(professor.getProfilePicture()));

            genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String gender = (String) parent.getItemAtPosition(position);
                    if (gender.equals("Feminino")) {
                        selectedGender = Gender.F;
                    } else if (gender.equals("Masculino")) {
                        selectedGender = Gender.M;
                    } else {
                        selectedGender = Gender.O;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(v -> deleteProfessor());

            saveButton.setOnClickListener(v -> saveProfessor());
        } else {
            textId.setVisibility(View.INVISIBLE);
            deleteButton.setEnabled(false);
            deleteButton.setVisibility(View.INVISIBLE);
        }

        profileImageView.setOnClickListener(v -> requestStoragePermission());

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

        profileImageView.setOnClickListener(v -> openImageChooser());
    }

    private void loadSubjects() {
        final String url = EGradeUtil.URL + "/api/v1/subject/findByCoordinatorId/" + DataHolder.getInstance().getCoordinator().getId();

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                Gson gson = new Gson();
                Type subjectListType = new TypeToken<List<Subject>>() {}.getType();
                subjectList = gson.fromJson(response, subjectListType);

                runOnUiThread(() -> setupSubjectSpinner());
            }

            @Override
            public void onFailure(String error) {
                Log.e("Erro", error);
            }
        });
    }

    private void setupSubjectSpinner() {
        if (subjectList == null) {
            return;
        }

        List<String> subjectNames = new ArrayList<>();
        for (Subject subject : subjectList) {
            subjectNames.add(subject.getName());
        }

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, subjectNames);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = subjectList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), PICK_IMAGE_REQUEST);
    }

    private void deleteProfessor() {
        final String url = EGradeUtil.URL + "/api/v1/professor/delete/" + professor.getId();

        HttpUtil.sendRequest(url, Method.DELETE, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Professor deletado com sucesso!", Toast.LENGTH_LONG).show();
                    DataHolder.getInstance().setCoordinator(coordinator);
                    setResult(RESULT_OK);
                    finish();
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Erro ao deletar professor! Erro:" + error, Toast.LENGTH_LONG).show();
                Log.e("Erro", error);
            }
        });
    }

    private void saveProfessor() {
        if (validateInputs()) {
            if (professor == null) {
                registerProfessor();
            } else {
                updateProfessor();
            }
        }
    }

    private boolean validateInputs() {
        if (nameEditText.getText().toString().isEmpty()) {
            nameEditText.setError("Nome é obrigatório");
            return false;
        }
        if (cpfEditText.getText().toString().length() != 14) {
            cpfEditText.setError("CPF inválido");
            return false;
        }
        if (emailEditText.getText().toString().isEmpty()) {
            emailEditText.setError("Email é obrigatório");
            return false;
        }
        if (phoneEditText.getText().length() < 14) {
            phoneEditText.setError("Telefone é obrigatório");
            return false;
        }
        if (birthDateEditText.getText().length() != 10) {
            birthDateEditText.setError("Data de Nascimento é obrigatória");
            return false;
        }
        return true;
    }

    private void registerProfessor() {
        final String url = EGradeUtil.URL + "/api/v1/professor/save";
        final String body = HttpUtil.generateRequestBody(
                "name", nameEditText.getText().toString(),
                "cpf", cpfEditText.getText().toString().replaceAll("[-.]", ""),
                "gender", selectedGender.toString(),
                "email", emailEditText.getText().toString(),
                "phoneNumber", phoneEditText.getText().toString().replaceAll("[() -]", ""),
                "birthDate", birthDateEditText.getText().toString(),
                "password", passwordEditText.getText().toString()   ,
                "active", Boolean.toString(activeCheckBox.isChecked()),
                "subjects",
                "profilePicture", selectedPhoto != null ? EGradeUtil.bitmapToBase64(selectedPhoto) : null
        );

        HttpUtil.sendRequest(url, Method.POST, body, new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Professor cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    DataHolder.getInstance().setCoordinator(coordinator);
                    setResult(RESULT_OK);
                    finish();
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Erro ao cadastrar professor! Erro:" + error, Toast.LENGTH_LONG).show();
                Log.e("Erro", error);
            }
        });
    }

    private void updateProfessor() {
        final String url = EGradeUtil.URL + "/api/v1/professor/update";
        final String body = HttpUtil.generateRequestBody(
                "id", String.valueOf(professor.getId()),
                "name", nameEditText.getText().toString(),
                "cpf", cpfEditText.getText().toString().replaceAll("[-.]", ""),
                "gender", selectedGender.toString(),
                "email", emailEditText.getText().toString(),
                "phoneNumber", phoneEditText.getText().toString().replaceAll("[() -]", ""),
                "birthDate", birthDateEditText.getText().toString(),
                "password", passwordEditText.getText().toString(),
                "active", Boolean.toString(activeCheckBox.isChecked()),
                "subjects", String.valueOf(selectedSubject.getId()),
                "profilePicture", selectedPhoto != null ? EGradeUtil.bitmapToBase64(selectedPhoto) : null
        );

        HttpUtil.sendRequest(url, Method.PUT, body, new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Professor atualizado com sucesso!", Toast.LENGTH_LONG).show();
                    DataHolder.getInstance().setCoordinator(coordinator);
                    setResult(RESULT_OK);
                    finish();
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Erro ao atualizar professor! Erro:" + error, Toast.LENGTH_LONG).show();
                    Log.e("Erro", error);
                });
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

