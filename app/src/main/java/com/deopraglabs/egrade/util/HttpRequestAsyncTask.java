package com.deopraglabs.egrade.util;

import android.os.AsyncTask;

import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Teacher;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import lombok.Getter;

public class HttpRequestAsyncTask extends AsyncTask<Void, Void, Integer> {

    private Map<String, String> params;
    private String urlString;
    private Method method;
    public int responseCode = -1;

    public HttpRequestAsyncTask(Map<String, String> params, String urlString, Method method) {
        this.params = params;
        this.urlString = urlString;
        this.method = method;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.toString());
            conn.setDoOutput(true);
            conn.setDoInput(true);

            StringBuilder postData = new StringBuilder();
            postData.append("{");
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (postData.length() != 0) postData.append("\n");
                postData.append('"');
                postData.append(param.getKey());
                postData.append('"');
                postData.append(": ");
                postData.append('"');
                postData.append(String.valueOf(param.getValue()));
                postData.append('"');
                postData.append(',');
            }
            postData.deleteCharAt(postData.length() - 1);
            postData.append("\n}");

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            try (OutputStream out = conn.getOutputStream()) {
                out.write(postDataBytes);
            }

            responseCode = conn.getResponseCode();

            conn.disconnect();

            return responseCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}