package dani.com.dialogflowassistance.logica.speech;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;

import dani.com.dialogflowassistance.Vista.MainActivity;

public class SpeechToText {
    private SpeechRecognizer recognizer;
    private MainActivity activity;

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
                activity.reconigtionResults(Objects.requireNonNull(results.getStringArrayList
                        (SpeechRecognizer
                                .RESULTS_RECOGNITION)).get(0));
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
