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
import com.deopraglabs.egrade.model.User;
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
            final String url = "https://www.google.com/";
            final Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }

    private boolean login(final String cpf, final String password) {
        final String url = "http://192.168.1.3:8080/api/v1/login";
        final String method = "POST";
        final String body = HttpUtil.generateRequestBody(
                "cpf", cpf,
                    "password", password
        );

        HttpUtil.sendRequest(url, method, body, new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                final Gson gson = new Gson();
                final User user = gson.fromJson(response, User.class);
                switch (user.getRole()) {
                    case ALUNO:
                        startActivity(new Intent(LoginActivity.this, UserActivity.class));
                        finish();
                        break;

                    case PROFESSOR:
                        startActivity(new Intent(LoginActivity.this, UserActivity.class));
                        finish();
                        break;

                    case COORDENADOR:
                        startActivity(new Intent(LoginActivity.this, UserActivity.class));
                        finish();
                        break;

                    default:
//                        Toast.makeText(LoginActivity.this, "Usuário e/ou senha incorretos!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(String error) {
//                Toast.makeText(LoginActivity.this, "Usuário e/ou senha incorretos!", Toast.LENGTH_SHORT).show();
                Log.e("Erro", error);
            }
        });
        return false;
    }
}
