package dani.com.dialogflowassistance.logica.async;


import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;

import dani.com.dialogflowassistance.Vista.MainActivity;

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
        activity.callbackDialogflow(detectIntentResponse);
    }
}
