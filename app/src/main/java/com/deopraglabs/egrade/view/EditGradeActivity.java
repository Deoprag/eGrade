package com.deopraglabs.egrade.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Grade;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.model.Subject;
import com.deopraglabs.egrade.util.DataHolder;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;

public class EditGradeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_IMAGE1 = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE2 = 2;

    private Student student;
    private Subject subject;
    private Grade grade;

    private EditText editTextN1;
    private EditText editTextN2;
    private TextView textViewTest1Status;
    private TextView textViewTest2Status;
    private Button buttonUploadTest1;
    private Button buttonUploadTest2;
    private Button buttonViewTest1;
    private Button buttonViewTest2;
    private Button buttonSave;

    private Bitmap selectedTest1;
    private Bitmap selectedTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grade);

        student = DataHolder.getInstance().getStudent();
        subject = DataHolder.getInstance().getSubject();

        if (student == null || subject == null) {
            Toast.makeText(this, "Dados inválidos", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editTextN1 = findViewById(R.id.editTextN1);
        editTextN2 = findViewById(R.id.editTextN2);
        textViewTest1Status = findViewById(R.id.textViewTest1Status);
        textViewTest2Status = findViewById(R.id.textViewTest2Status);
        buttonUploadTest1 = findViewById(R.id.buttonUploadTest1);
        buttonUploadTest2 = findViewById(R.id.buttonUploadTest2);
        buttonViewTest1 = findViewById(R.id.buttonViewTest1);
        buttonViewTest2 = findViewById(R.id.buttonViewTest2);
        buttonSave = findViewById(R.id.buttonSave);

        grade = findGradeForSubject(student, subject);
        if (grade == null) {
            grade = new Grade();
            grade.setSubject(subject);
            student.getGrades().add(grade);
        } else {
            loadGradeData(grade);
        }

        buttonUploadTest1.setOnClickListener(v -> openGallery(REQUEST_CODE_SELECT_IMAGE1));
        buttonUploadTest2.setOnClickListener(v -> openGallery(REQUEST_CODE_SELECT_IMAGE2));

        buttonViewTest1.setOnClickListener(v -> {
            if (selectedTest1 != null) {
                showImageDialog(selectedTest1);
            } else if (grade.getTest1() != null) {
                Bitmap bitmap = EGradeUtil.base64ToBitmap(grade.getTest1());
                showImageDialog(bitmap);
            } else {
                Toast.makeText(EditGradeActivity.this, "Teste 1 não disponível", Toast.LENGTH_SHORT).show();
            }
        });

        buttonViewTest2.setOnClickListener(v -> {
            if (selectedTest2 != null) {
                showImageDialog(selectedTest2);
            } else if (grade.getTest2() != null) {
                Bitmap bitmap = EGradeUtil.base64ToBitmap(grade.getTest2());
                showImageDialog(bitmap);
            } else {
                Toast.makeText(EditGradeActivity.this, "Teste 2 não disponível", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSave.setOnClickListener(v -> chooseSave());
    }

    private Grade findGradeForSubject(Student student, Subject subject) {
        for (Grade g : student.getGrades()) {
            if (g.getSubject().getId() == (subject.getId())) {
                return g;
            }
        }
        return null;
    }

    private void loadGradeData(Grade grade) {
        editTextN1.setText(String.valueOf(grade.getN1()));
        editTextN2.setText(String.valueOf(grade.getN2()));
        if (grade.getTest1() != null) {
            textViewTest1Status.setText("Imagem anexada");
        }
        if (grade.getTest2() != null) {
            textViewTest2Status.setText("Imagem anexada");
        }
    }

    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                if (requestCode == REQUEST_CODE_SELECT_IMAGE1) {
                    selectedTest1 = bitmap;
                    textViewTest1Status.setText("Imagem selecionada");
                    grade.setTest1(EGradeUtil.bitmapToBase64(bitmap));
                } else if (requestCode == REQUEST_CODE_SELECT_IMAGE2) {
                    selectedTest2 = bitmap;
                    textViewTest2Status.setText("Imagem selecionada");
                    grade.setTest2(EGradeUtil.bitmapToBase64(bitmap));
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void chooseSave() {
        if (grade.getId() > 0) {
            updateGrade();
        } else {
            saveGrade();
        }
    }

    private void updateGrade() {
        final String url = EGradeUtil.URL + "/api/v1/grade/update";
        final String body = HttpUtil.generateRequestBody(
                "id", String.valueOf(grade.getId()),
                "n1", editTextN1.getText().toString(),
                "n2", editTextN2.getText().toString(),
                "test1", selectedTest1 != null ? EGradeUtil.bitmapToBase64(selectedTest1) : null,
                "test2", selectedTest2 != null ? EGradeUtil.bitmapToBase64(selectedTest2) : null,
                "student_id", String.valueOf(student.getId()),
                "subject_id", String.valueOf(subject.getId())
        );

        HttpUtil.sendRequest(url, Method.PUT, body, new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Notas salvas com sucesso!", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Erro ao salvar notas! Erro:" + error, Toast.LENGTH_LONG).show();
                Log.e("Erro", error);
            }
        });
    }

    private void saveGrade() {
        final String url = EGradeUtil.URL + "/api/v1/grade/save";
        final String body = HttpUtil.generateRequestBody(
                "n1", editTextN1.getText().toString(),
                "n2", editTextN2.getText().toString(),
                "test1", selectedTest1 != null ? EGradeUtil.bitmapToBase64(selectedTest1) : null,
                "test2", selectedTest2 != null ? EGradeUtil.bitmapToBase64(selectedTest2) : null,
                "student_id", String.valueOf(student.getId()),
                "subject_id", String.valueOf(subject.getId())
        );

        HttpUtil.sendRequest(url, Method.POST, body, new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Notas salvas com sucesso!", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Erro ao salvar notas! Erro:" + error, Toast.LENGTH_LONG).show();
                Log.e("Erro", error);
            }
        });
    }

    private void showImageDialog(Bitmap bitmap) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_image_viewer);
        PhotoView photoView = dialog.findViewById(R.id.photoView);
        photoView.setImageBitmap(bitmap);
        dialog.show();
    }
}
