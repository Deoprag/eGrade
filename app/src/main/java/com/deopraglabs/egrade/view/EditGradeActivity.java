package com.deopraglabs.egrade.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Grade;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.model.Subject;
import com.deopraglabs.egrade.util.DataHolder;
import com.deopraglabs.egrade.util.EGradeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditGradeActivity extends AppCompatActivity {

    private Student student;
    private Subject subject;
    private Grade grade;

    private EditText editTextN1;
    private EditText editTextN2;
    private Button buttonDownloadTest1;
    private Button buttonDownloadTest2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grade);

        // Obter student e subject da DataHolder
        student = DataHolder.getInstance().getStudent();
        subject = DataHolder.getInstance().getSubject();

        if (student == null || subject == null) {
            Toast.makeText(this, "Dados inválidos", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editTextN1 = findViewById(R.id.editTextN1);
        editTextN2 = findViewById(R.id.editTextN2);
        buttonDownloadTest1 = findViewById(R.id.buttonDownloadTest1);
        buttonDownloadTest2 = findViewById(R.id.buttonDownloadTest2);

        // Verificar se o aluno já possui notas para a matéria
        grade = findGradeForSubject(student, subject);
        if (grade == null) {
            grade = new Grade();
            grade.setSubject(subject);
            student.getGrades().add(grade);
        } else {
            loadGradeData(grade);
        }

        buttonDownloadTest1.setOnClickListener(v -> {
            if (grade.getTest1() != null) {
                Bitmap bitmap = EGradeUtil.base64ToBitmap(grade.getTest1());
                saveBitmapToFile(bitmap, subject.getName() + "_N1.png");
            } else {
                Toast.makeText(EditGradeActivity.this, "Teste 1 não disponível", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDownloadTest2.setOnClickListener(v -> {
            if (grade.getTest2() != null) {
                Bitmap bitmap = EGradeUtil.base64ToBitmap(grade.getTest2());
                saveBitmapToFile(bitmap, subject.getName() + "_N2.png");
            } else {
                Toast.makeText(EditGradeActivity.this, "Teste 2 não disponível", Toast.LENGTH_SHORT).show();
            }
        });
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
    }

    private void saveBitmapToFile(Bitmap bitmap, String fileName) {
        File file = new File(getExternalFilesDir(null), fileName);
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Toast.makeText(this, "Imagem salva em: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao salvar imagem", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
