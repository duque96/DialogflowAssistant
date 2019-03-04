package dani.com.dialogflowassistance.Vista;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dani.com.dialogflowassistance.R;
import dani.com.dialogflowassistance.logica.async.AsyncDialogflow;
import dani.com.dialogflowassistance.logica.externalApps.ExternalAppManage;
import dani.com.dialogflowassistance.logica.messageList.MessageListAdapter;
import dani.com.dialogflowassistance.logica.model.Message;
import dani.com.dialogflowassistance.logica.model.SendBird;
import dani.com.dialogflowassistance.logica.model.TextMessage;
import dani.com.dialogflowassistance.logica.model.User;
import dani.com.dialogflowassistance.logica.model.UserDialogflow;
import dani.com.dialogflowassistance.logica.model.UserSender;
import dani.com.dialogflowassistance.logica.responseType.ResponseTypeManager;
import dani.com.dialogflowassistance.logica.speech.Speaker;
import dani.com.dialogflowassistance.logica.speech.SpeechToText;

public class MainActivity extends AppCompatActivity {
    // View
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    // Logica
    private SpeechToText reconigzer;
    private Speaker tts;
    private List<Message> messageList;
    private User currentUser;
    private User assistant;

    // Dialogflow
    private SessionName session;
    private String uuid;
    private SessionsClient sessionsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat);

        getSupportActionBar().hide();

        reconigzer = new SpeechToText(this);
        tts = new Speaker();

        uuid = UUID.randomUUID().toString();

        validar();
        setCredentials();

        //User
        currentUser = new UserSender();
        SendBird.setCurrentUser(currentUser);
        assistant = new UserDialogflow();

        // Message list
        mMessageRecycler = findViewById(R.id.reyclerview_message_list);

        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

        messageList = new ArrayList<>();

        mMessageAdapter = new MessageListAdapter(messageList);

        mMessageRecycler.setAdapter(mMessageAdapter);

    }

    private void setCredentials() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.dialogflow_credentials);
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

    /**
     * Habilita el reconocimiento de voz
     *
     * @param view view
     */
    public void activarMicrofono(View view) {
        reconigzer.recognizeSpeech();
    }

    public void reconigtionResults(String recognitionText) {
        createSendMessage(recognitionText);
        if (recognitionText.trim().isEmpty()) {
            Toast.makeText(this, "No se ha detectado audio", Toast.LENGTH_SHORT).show();
        } else {
            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder()
                    .setText(recognitionText).setLanguageCode("es-ES")).build();
            new AsyncDialogflow(this, session, queryInput, sessionsClient).execute();
        }
    }

    public void callbackDialogflow(DetectIntentResponse response) {
        if (response != null) {
            ResponseTypeManager responseTypeManager =
                    new ResponseTypeManager(response.getQueryResult(), getApplicationContext(),
                            messageList, assistant);
            responseTypeManager.createResponse();

            int newMsgPosition = messageList.size() - 1;

            // Notify recycler view insert one new data.
            mMessageAdapter.notifyItemInserted(newMsgPosition);

            // Scroll RecyclerView to the last message.
            mMessageRecycler.scrollToPosition(newMsgPosition);

        } else {
            Toast.makeText(this, "Error de comunicación", Toast.LENGTH_SHORT).show();
        }
    }

    private void validar() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            solicitarPermiso();
        }
    }

    private void solicitarPermiso() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);
        }
    }

    private void createSendMessage(String text) {
        messageList.add(new TextMessage(currentUser, text.substring(0, 1).toUpperCase() + text
                .substring(1)));

        int newMsgPosition = messageList.size() - 1;

        // Notify recycler view insert one new data.
        mMessageAdapter.notifyItemInserted(newMsgPosition);

        // Scroll RecyclerView to the last message.
        mMessageRecycler.scrollToPosition(newMsgPosition);
    }

    public void displayApp(View view) {
        ExternalAppManage app = new ExternalAppManage();
        app.display((String[])view.getTag(), getApplicationContext());
    }
}

