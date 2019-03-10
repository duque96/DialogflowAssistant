package dani.com.dialogflowassistance.logica.speech;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.TextInput;

import java.util.Locale;
import java.util.Objects;

import dani.com.dialogflowassistance.Vista.MainActivity;
import dani.com.dialogflowassistance.logica.async.AsyncDialogflow;
import dani.com.dialogflowassistance.logica.dialogflowCredentials.DialogflowCredentials;

public class SpeechToText {
    private SpeechRecognizer recognizer;

    public SpeechToText(final MainActivity activity) {
        this.recognizer = SpeechRecognizer.createSpeechRecognizer(activity);

        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Toast.makeText(activity, "Escuchando", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                Toast.makeText(activity, "Silenciado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                String recognitionText = Objects.requireNonNull(results
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)).get(0);

                activity.createSendMessage(recognitionText);
                if (recognitionText.trim().isEmpty()) {
                    Toast.makeText(activity, "No se ha detectado audio", Toast.LENGTH_SHORT).show();
                } else {
                    QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder()
                            .setText(recognitionText).setLanguageCode("es-ES")).build();
                    new AsyncDialogflow(activity, DialogflowCredentials.getInstance().getSessionName(),
                            queryInput, DialogflowCredentials.getInstance().getSessionsClient()).execute();
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

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
}
