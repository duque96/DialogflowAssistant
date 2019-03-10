package dani.com.dialogflowassistance.Vista;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dani.com.dialogflowassistance.R;
import dani.com.dialogflowassistance.logica.dialogflowCredentials.DialogflowCredentials;
import dani.com.dialogflowassistance.logica.externalApps.ExternalAppManage;
import dani.com.dialogflowassistance.logica.messageList.MessageListAdapter;
import dani.com.dialogflowassistance.logica.model.Message;
import dani.com.dialogflowassistance.logica.model.SendBird;
import dani.com.dialogflowassistance.logica.model.TextMessage;
import dani.com.dialogflowassistance.logica.model.User;
import dani.com.dialogflowassistance.logica.model.UserDialogflow;
import dani.com.dialogflowassistance.logica.model.UserSender;
import dani.com.dialogflowassistance.logica.responseType.ResponseTypeManager;
import dani.com.dialogflowassistance.logica.speech.SpeechToText;
import dani.com.dialogflowassistance.logica.util.Permiso;

public class MainActivity extends AppCompatActivity {
    // View
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    // Logica
    private SpeechToText reconigzer;
    private List<Message> messageList;
    private User currentUser;
    private User assistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat);

        Objects.requireNonNull(getSupportActionBar()).hide();

        reconigzer = new SpeechToText(this);

        // Solicitamos los permisos
        Permiso.getInstance().validar(this);

        //Proporcionamos las credenciales de Google
        DialogflowCredentials.getInstance().setCredentials(this);

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

    /**
     * Habilita el reconocimiento de voz
     *
     * @param view view
     */
    public void activarMicrofono(View view) {
        reconigzer.recognizeSpeech();
    }

    /**
     * Procesa la respuesta obtenida del servidor de Dialogflow
     *
     * @param response respuesta del servidor de Dialogflow
     */
    public void callbackDialogflow(DetectIntentResponse response) {
        if (response != null) {
            ResponseTypeManager responseTypeManager =
                    new ResponseTypeManager(response.getQueryResult(), getApplicationContext(),
                            messageList, assistant);
            responseTypeManager.createResponseMessage();

            int newMsgPosition = messageList.size() - 1;
            mMessageAdapter.notifyItemInserted(newMsgPosition);
            mMessageRecycler.scrollToPosition(newMsgPosition);
        } else {
            Toast.makeText(this, "Error de comunicación", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Crea un nuevo mensaje a partir de la entrada por voz del usuario
     * @param text texto obtenido a partir del reconocimiento de voz
     */
    public void createSendMessage(String text) {
        messageList.add(new TextMessage(currentUser, text.substring(0, 1).toUpperCase() + text
                .substring(1)));

        int newMsgPosition = messageList.size() - 1;
        mMessageAdapter.notifyItemInserted(newMsgPosition);
        mMessageRecycler.scrollToPosition(newMsgPosition);
    }

    /**
     *
     * @param view view
     */
    public void displayApp(View view) {
        ExternalAppManage app = new ExternalAppManage();
        app.display((String[]) view.getTag(), getApplicationContext());
    }
}

