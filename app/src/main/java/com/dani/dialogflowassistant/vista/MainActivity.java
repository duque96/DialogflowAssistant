package com.dani.dialogflowassistant.vista;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dani.appupdateinstaller.AppUpdateInstaller;
import com.dani.dialogflowassistant.BuildConfig;
import com.dani.dialogflowassistant.R;
import com.dani.dialogflowassistant.logica.actions.ButtonAction;
import com.dani.dialogflowassistant.logica.adapter.MessageListAdapter;
import com.dani.dialogflowassistant.logica.credentials.DialogflowCredentials;
import com.dani.dialogflowassistant.logica.model.Message;
import com.dani.dialogflowassistant.logica.model.SendBird;
import com.dani.dialogflowassistant.logica.model.User;
import com.dani.dialogflowassistant.logica.model.UserDialogflow;
import com.dani.dialogflowassistant.logica.model.UserSender;
import com.dani.dialogflowassistant.logica.speech.Speaker;
import com.dani.dialogflowassistant.logica.speech.SpeechToText;
import com.dani.dialogflowassistant.logica.util.Permiso;
import com.dani.dialogflowassistant.logica.util.Utils;
import com.dani.dialogflowassistant.logica.util.ViewGroupUtils;
import com.dani.dialogflowassistant.vista.bottomsheet.DisableBottomSheet;
import com.dani.dialogflowassistant.vista.speechview.SpeechRecognitionView;
import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.cloud.dialogflow.v2.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Handler.Callback {
    // View
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private FloatingActionButton recordingButton;
    private SpeechRecognitionView speechRecognitionView;
    private DisableBottomSheet disableBottomSheet;

    // Logica
    private SpeechToText reconigzer;
    private Speaker textToSpeech;
    private List<Message> messageList;
    private User currentUser;
    private User assistant;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Evitar que la pantalla se apague
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
        mMessageAdapter = new MessageListAdapter(messageList, this);
        mMessageRecycler.setAdapter(mMessageAdapter);

        // Comprobaciones
        checkInternetConnection(this);

        //BottomSheetBehaviour
        disableBottomSheet = (DisableBottomSheet) DisableBottomSheet.from(findViewById(R.id.bottom_sheet));
        disableBottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_EXPANDED && !disableBottomSheet.isDisabled()) {
                    disableBottomSheet.setDisabled();
                    changeBottomSheetBackground();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        createWelcomeMessage();

        slideToTop(findViewById(R.id.coordinatorLayout));
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
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(activity,
                            R.drawable.dialog_round));
                }
            });

            dialog.show();
        } else {
            String json = "https://raw.githubusercontent.com/duque96/DialogflowAssistant/master/app/update-changelog.json";
            AppUpdateInstaller installer = new AppUpdateInstaller(this, "DialogflowAssistant",
                    R.layout.activity_main, this, json, BuildConfig.APPLICATION_ID);
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

    public void addMessages(final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (disableBottomSheet.getState() == BottomSheetBehavior.STATE_COLLAPSED && messageList.size() == 2 && message.getSender().equals(assistant)) {
                    disableBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                    changeBottomSheetBackground();
                }
            }
        });

        if (messageList.size() > 0 && messageList.get(messageList.size() - 1).getMessage().getMessageCase().equals(Intent.Message.MessageCase.SUGGESTIONS)) {
            removeMessage(messageList.size() - 1);
        }

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

    public void slideToTop(View view) {

        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationY",
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
                        getResources().getDisplayMetrics()));
        animation.setDuration(500);
        animation.start();
    }

    private void createWelcomeMessage() {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.lightTransparent));
        reconigzer.handleRecognizedText("Bienvenida", true);
        ViewGroupUtils.micToSpeechView();
    }

    private void changeBottomSheetBackground() {
        View v = findViewById(R.id.bottom_sheet);
        v.setBackground(null);
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    public List<Message> getMessageList() {
        return new ArrayList<>(messageList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null)
            textToSpeech.stopSpeaking();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textToSpeech != null)
            textToSpeech.stopSpeaking();
    }

    public Speaker getSpeaker() {
        textToSpeech = new Speaker();
        return textToSpeech;
    }

    public void categorySelect(View v) {
        reconigzer.stopListening();
        ViewGroupUtils.speechToMicView();

        String text = ((Chip) v).getText().toString();

        reconigzer.handleRecognizedText(text, false);
    }

    private void removeMessage(int position) {
        messageList.remove(position);
        mMessageAdapter.notifyItemRemoved(position);
        mMessageAdapter.notifyItemRangeChanged(position, messageList.size());
    }

    protected void onResume() {
        super.onResume();
    }

    public void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
    }
}