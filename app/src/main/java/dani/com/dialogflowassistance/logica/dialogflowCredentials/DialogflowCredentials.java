package dani.com.dialogflowassistance.logica.dialogflowCredentials;

import android.app.Activity;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import dani.com.dialogflowassistance.R;

public class DialogflowCredentials {
    // Dialogflow
    private SessionName session;
    private String uuid;
    private SessionsClient sessionsClient;
    private static DialogflowCredentials credentials;

    private DialogflowCredentials() {
        uuid = UUID.randomUUID().toString();
    }

    public static DialogflowCredentials getInstance() {
        if (credentials == null)
            credentials = new DialogflowCredentials();

        return credentials;
    }

    public void setCredentials(Activity activity) {
        try {
            InputStream stream = activity.getResources().openRawResource(R.raw.dialogflow_credentials);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();

            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectId, uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SessionName getSessionName() {
        return session;
    }

    //public String getUuid() {
      //  return uuid;
    //}

    public SessionsClient getSessionsClient() {
        return sessionsClient;
    }
}
