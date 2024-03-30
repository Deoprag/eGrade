package com.deopraglabs.egrade.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.util.HttpRequestAsyncTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = findViewById(R.id.login_button);
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);

        loginButton.setOnClickListener(view -> {
            if(login(username.getText().toString(), password.getText().toString())) {
                startActivity(new Intent(LoginActivity.this, UserActivity.class));
                finish();
            } else {

            }
        });
    }

    private boolean login(String cpf, String password) {
        final Map<String, String> requestMap = new HashMap<>();

        requestMap.put("cpf", cpf);
        requestMap.put("password", password);

        HttpRequestAsyncTask hrat2 = new HttpRequestAsyncTask(requestMap, "http://192.168.1.6:8080/api/v1/user/login");
        hrat2.execute();

        try {
            if(hrat2.get().equals(200)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}