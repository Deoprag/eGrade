package com.deopraglabs.egrade.util;

import android.util.Log;

import com.deopraglabs.egrade.model.Method;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    public interface HttpRequestListener {
        void onSuccess(String response);
        void onFailure(String error);
    }

    public static void sendRequest(String url, Method method, String body, HttpRequestListener listener) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = method == Method.GET ? null : RequestBody.create(mediaType, body);

        Request request = new Request.Builder()
                .url(url)
                .method(method.toString(), requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    listener.onSuccess(responseBody);
                } else {
                    listener.onFailure("Erro: " + response);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure("Erro: " + e.getMessage());
            }
        });
    }

    public static String generateRequestBody(String... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("O número de argumentos deve ser par");
        }

        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();

        for (int i = 0; i < keyValuePairs.length; i += 2) {
            String key = keyValuePairs[i];
            String value = keyValuePairs[i + 1];
            jsonObject.addProperty(key, value);
        }

        return gson.toJson(jsonObject);
    }
}