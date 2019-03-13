package com.dani.dialogflowassistant.logica.async;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dani.dialogflowassistant.vista.MainActivity;
import com.dani.dialogflowassistant.logica.response.ResponseTypeManager;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;

public class AsyncDialogflow extends AsyncTask<String, Void, DetectIntentResponse> {
    @SuppressLint("StaticFieldLeak")
    private MainActivity activity;
    private SessionName session;
    private QueryInput queryInput;
    private SessionsClient sessionsClient;

    public AsyncDialogflow(MainActivity activity, SessionName session, QueryInput queryInput,
                           SessionsClient sessionsClient) {
        this.activity = activity;
        this.session = session;
        this.queryInput = queryInput;
        this.sessionsClient = sessionsClient;
    }


    @Override
    protected DetectIntentResponse doInBackground(String... String) {
        DetectIntentRequest detectIntentRequest = DetectIntentRequest.newBuilder()
                .setSession(session.toString()).setQueryInput(queryInput).build();
        return sessionsClient.detectIntent(detectIntentRequest);
    }

    @Override
    protected void onPostExecute(DetectIntentResponse detectIntentResponse) {
        if (detectIntentResponse != null) {
            ResponseTypeManager responseTypeManager =
                    new ResponseTypeManager(detectIntentResponse.getQueryResult(), activity, activity.getAssistant());

            responseTypeManager.createResponseMessage();
        } else {
            Toast.makeText(activity, "Error de comunicación", Toast.LENGTH_SHORT).show();
        }
    }
}
