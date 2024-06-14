package com.deopraglabs.egrade.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.User;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Professor;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = findViewById(R.id.login_button);
        final Button problemButton = findViewById(R.id.problem_button);
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);

        loginButton.setOnClickListener(view -> {
            login(username.getText().toString(), password.getText().toString());
        });

        problemButton.setOnClickListener(view -> {
            final String url = "https://docs.google.com/forms/d/e/1FAIpQLSfvfqBnRlb-2erRVYynsGKRQpDE6i8X3NfpjVEubdpvhbRgGA/viewform?usp=sf_link";
            final Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }

    private boolean login(final String cpf, final String password) {
        final String url = EGradeUtil.URL + "/api/v1/login";
        final String body = HttpUtil.generateRequestBody(
                "cpf", cpf,
                    "password", password
        );

        HttpUtil.sendRequest(url, Method.POST, body, new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                final Gson gson = new Gson();
                final User user = gson.fromJson(response, User.class);
                Intent intent;

                switch (user.getRole()) {
                    case COORDENADOR:
                        final Coordinator coordinator = gson.fromJson(response, Coordinator.class);
                        intent = new Intent(LoginActivity.this, CoordinatorActivity.class);
                        intent.putExtra("user", coordinator);
                        startActivity(intent);
                        finish();
                        break;

                    case PROFESSOR:
                        final Professor professor = gson.fromJson(response, Professor.class);
                        intent = new Intent(LoginActivity.this, ProfessorActivity.class);
                        intent.putExtra("user", professor);
                        startActivity(intent);
                        finish();
                        break;

                    case ALUNO:
                        final Student student = gson.fromJson(response, Student.class);
                        intent = new Intent(LoginActivity.this, StudentActivity.class);
                        intent.putExtra("user", student);
                        startActivity(intent);
                        finish();
                        break;

                    default:
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "Usuário e/ou senha incorretos!", Toast.LENGTH_SHORT).show();
                        });
                        break;
                }
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Usuário e/ou senha incorretos!", Toast.LENGTH_SHORT).show();
                });
                Log.e("Erro", error);
            }
        });
        return false;
    }
}
