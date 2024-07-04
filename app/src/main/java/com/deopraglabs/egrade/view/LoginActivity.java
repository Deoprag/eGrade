package com.deopraglabs.egrade.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.deopraglabs.egrade.util.DataHolder;
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

        username.addTextChangedListener(new TextWatcher() {

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
                    username.setText(str);
                    username.setSelection(username.getText().length());
                } else {
                    old = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean login(final String cpf, final String password) {
        final String url = EGradeUtil.URL + "/api/v1/login";
        final String body = HttpUtil.generateRequestBody(
                "cpf", cpf.replaceAll("[-.]", ""),
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
                        intent = new Intent(LoginActivity.this, CoordinatorActivity.class);
                        DataHolder.setCoordinator(gson.fromJson(response, Coordinator.class));
                        startActivity(intent);
                        finish();
                        break;

                    case PROFESSOR:
                        intent = new Intent(LoginActivity.this, ProfessorActivity.class);
                        DataHolder.setProfessor(gson.fromJson(response, Professor.class));
                        startActivity(intent);
                        finish();
                        break;

                    case ALUNO:
                        intent = new Intent(LoginActivity.this, StudentActivity.class);
                        DataHolder.setStudent(gson.fromJson(response, Student.class));
                        startActivity(intent);
                        finish();
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
