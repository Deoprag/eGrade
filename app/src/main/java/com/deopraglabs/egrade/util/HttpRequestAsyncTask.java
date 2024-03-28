package com.deopraglabs.egrade.util;

import android.os.AsyncTask;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpRequestAsyncTask extends AsyncTask<Void, Void, Integer> {

    private Map<String, String> params;
    private String urlString;

    public HttpRequestAsyncTask(Map<String, String> params, String urlString) {
        this.params = params;
        this.urlString = urlString;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            // Create URL object
            URL url = new URL(urlString);

            // Create connection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set request method to POST
            conn.setRequestMethod("POST");

            // Enable output and input streams
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Construct the request parameters
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

            System.out.println(postData);
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            try (OutputStream out = conn.getOutputStream()) {
                out.write(postDataBytes);
            }
            System.out.println(postDataBytes);

            int responseCode = conn.getResponseCode();

            conn.disconnect();

            return responseCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return a special code for network error
        }
    }

    @Override
    protected void onPostExecute(Integer responseCode) {
        if (responseCode != -1) {
            System.out.println("Response Code: " + responseCode);
        } else {
            System.out.println("Network Error!");
        }
    }
}
