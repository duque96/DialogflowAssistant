package com.dani.dialogflowassistant.vista;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dani.appupdateinstaller.AppUpdateInstaller;
import com.dani.dialogflowassistant.R;
import com.dani.dialogflowassistant.logica.actions.ButtonAction;
import com.dani.dialogflowassistant.logica.adapter.MessageListAdapter;
import com.dani.dialogflowassistant.logica.credentials.DialogflowCredentials;
import com.dani.dialogflowassistant.logica.model.Message;
import com.dani.dialogflowassistant.logica.model.SendBird;
import com.dani.dialogflowassistant.logica.model.User;
import com.dani.dialogflowassistant.logica.model.UserDialogflow;
import com.dani.dialogflowassistant.logica.model.UserSender;
import com.dani.dialogflowassistant.logica.speech.SpeechToText;
import com.dani.dialogflowassistant.logica.util.Permiso;
import com.dani.dialogflowassistant.logica.util.Utils;
import com.dani.dialogflowassistant.logica.util.ViewGroupUtils;
import com.dani.dialogflowassistant.vista.speechview.SpeechRecognitionView;
import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Handler.Callback {
    // View
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private FloatingActionButton recordingButton;
    private SpeechRecognitionView speechRecognitionView;

    // Logica
    private SpeechToText reconigzer;
    private List<Message> messageList;
    private User currentUser;
    private User assistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Views
        speechRecognitionView = new SpeechRecognitionView(this);
        recordingButton = findViewById(R.id.floatingActionButton);
        ViewGroupUtils.setActivity(this);

        reconigzer = new SpeechToText(this, speechRecognitionView);

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

        // Comprobaciones
        checkInternetConnection(this);
    }

    private void checkInternetConnection(final Activity activity) {
        if (!Utils.isNetworkAvailable(activity)) {
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("No hay conexión de red")
                    .setMessage("Comprueba tus ajustes de red e inténtalo de nuevo")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    }).create();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_round));
                }
            });

            dialog.show();
        } else {
            String json = "https://raw.githubusercontent.com/duque96/DialogflowAssistant/master/app/update-changelog.json";
            AppUpdateInstaller installer = new AppUpdateInstaller(this, "DialogflowAssistant",
                    R.layout.activity_main, this, json);
            installer.execute();
        }
    }

    /**
     * Habilita el reconocimiento de voz
     *
     * @param view view
     */
    public void activarMicrofono(View view) {
        reconigzer.recognizeSpeech();
        speechRecognitionView.getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                reconigzer.recognizeSpeech();
            }
        }, 50);
    }

    /**
     * @param view view
     */
    public void displayApp(View view) {
        ButtonAction externalAppManager = new ButtonAction();
        externalAppManager.action((String[]) view.getTag(), this);
    }

    public void addMessages(Message message) {
        messageList.add(message);

        int newMsgPosition = messageList.size() - 1;
        mMessageAdapter.notifyItemInserted(newMsgPosition);
        mMessageRecycler.scrollToPosition(newMsgPosition);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User getAssistant() {
        return assistant;
    }

    public View getRecordingButton() {
        return recordingButton;
    }

    public RecognitionProgressView getSpeechRecognitionView() {
        return speechRecognitionView.getView();
    }

    @Override
    public boolean handleMessage(android.os.Message msg) {
        return true;
    }
}

