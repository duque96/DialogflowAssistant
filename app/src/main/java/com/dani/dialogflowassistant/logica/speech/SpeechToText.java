package com.dani.dialogflowassistant.logica.speech;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import com.dani.dialogflowassistant.logica.async.AsyncDialogflow;
import com.dani.dialogflowassistant.logica.credentials.DialogflowCredentials;
import com.dani.dialogflowassistant.logica.model.TextMessage;
import com.dani.dialogflowassistant.logica.util.ViewGroupUtils;
import com.dani.dialogflowassistant.vista.MainActivity;
import com.dani.dialogflowassistant.vista.speechview.SpeechRecognitionView;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.TextInput;

import java.util.Locale;
import java.util.Objects;

public class SpeechToText {
    private SpeechRecognizer recognizer;
    private MainActivity activity;


    public SpeechToText(MainActivity activity, final SpeechRecognitionView speechRecognitionView) {
        this.activity = activity;
        this.recognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        speechRecognitionView.setSpeechReconigzer(recognizer);

        speechRecognitionView.getView().setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                ViewGroupUtils.micToSpeechView();
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.i("SpeechBeginning", "User started to speak");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // Without use
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // Without use
            }

            @Override
            public void onEndOfSpeech() {
                Log.i("SpeechEnd", "User stops speaking");
            }

            @Override
            public void onError(int error) {
                if (error != SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
                    ViewGroupUtils.speechToMicView();
                    Log.e("ERROR", String.valueOf(error));
                }
            }

            @Override
            public void onResults(Bundle results) {
                String recognitionText = Objects.requireNonNull(results
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)).get(0);

                handleRecognizedText(recognitionText, false);
            }


            @Override
            public void onPartialResults(Bundle partialResults) {
                // Without use
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // Without use
            }
        });
    }

    public void recognizeSpeech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        recognizer.startListening(intent);
    }

    public void handleRecognizedText(String recognitionText, boolean welcome) {
        if (recognitionText.trim().isEmpty()) {
            Toast.makeText(activity, "No se ha detectado audio", Toast.LENGTH_SHORT).show();
        } else {

            String formatedText =
                    recognitionText.substring(0, 1).toUpperCase() + recognitionText.substring(1);

            com.google.cloud.dialogflow.v2.Intent.Message.Text text =
                    com.google.cloud.dialogflow.v2.Intent.Message.Text.newBuilder().addText(formatedText).build();

            com.google.cloud.dialogflow.v2.Intent.Message message =
                    com.google.cloud.dialogflow.v2.Intent.Message.getDefaultInstance().newBuilderForType().setText(text).build();
            if (!welcome)
                activity.addMessages(new TextMessage(activity.getCurrentUser(), message, null));

            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder()
                    .setText(recognitionText).setLanguageCode("es-ES")).build();

            new AsyncDialogflow(activity, DialogflowCredentials.getInstance().getSessionName(),
                    queryInput, DialogflowCredentials.getInstance().getSessionsClient()).execute();
        }
    }

    public void stopListening() {
        recognizer.stopListening();
    }
}
