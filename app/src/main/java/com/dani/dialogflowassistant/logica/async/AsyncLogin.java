package com.dani.dialogflowassistant.logica.async;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dani.dialogflowassistant.R;
import com.dani.dialogflowassistant.vista.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncLogin extends AsyncTask<String, Void, HttpURLConnection> {
    @SuppressLint("StaticFieldLeak")
    private LoginActivity activity;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;

    public AsyncLogin(LoginActivity activity) {
        this.activity = activity;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressBar = activity.findViewById(R.id.progressBarLogin);
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected HttpURLConnection doInBackground(String... strings) {
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL("https://dialogflownodewebhook.herokuapp.com/login"); //Enter URL here
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", strings[0]);
            jsonObject.put("password", strings[1]);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
            wr.flush();
            wr.close();

            return httpURLConnection;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(HttpURLConnection httpURLConnection) {
        progressBar.setVisibility(ProgressBar.GONE);

        try {
            if (httpURLConnection != null) {
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        activity.saveUserId(sb);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Toast.makeText(activity,
                            activity.getResources().getString(R.string.login_unauthorized),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity,
                        activity.getResources().getString(R.string.login_error),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
