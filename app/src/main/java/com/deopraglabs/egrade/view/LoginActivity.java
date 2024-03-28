package com.deopraglabs.egrade.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

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

        final Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            final Map<String, String> requestMap = new HashMap<>();
            requestMap.put("name", "Pedro Teste");
            requestMap.put("email", "pdroesofiarabelo@gmail.com");
            requestMap.put("role", "ALUNO");
            requestMap.put("birthDate", "27/02/2004");
            requestMap.put("cpf", "10938448512");
            requestMap.put("phoneNumber", "41999999992");
            requestMap.put("password", "Pedro123");
            HttpRequestAsyncTask hrat = new HttpRequestAsyncTask(requestMap, "http://192.168.1.6:8080/api/v1/user/save");
            hrat.execute();
        });
    }
}