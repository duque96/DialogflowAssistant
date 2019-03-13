package com.dani.dialogflowassistant.vista.speechview;

import android.app.Activity;
import android.speech.SpeechRecognizer;
import android.view.ViewGroup;

import com.dani.dialogflowassistant.R;
import com.github.zagum.speechrecognitionview.RecognitionProgressView;

import androidx.core.content.ContextCompat;

public class SpeechRecognitionView {
    private RecognitionProgressView recognitionProgressView;

    public SpeechRecognitionView(Activity activity) {
        int[] colors = new int[]{ContextCompat.getColor(activity, R.color.colorMessageRecieved),
                ContextCompat.getColor(activity, R.color.colorMessageSent),
                ContextCompat.getColor(activity, R.color.colorMessageRecieved),
                ContextCompat.getColor(activity, R.color.colorMessageSent),
                ContextCompat.getColor(activity, R.color.colorMessageRecieved)};
        int[] heights = {24, 28, 22, 27, 20};

        recognitionProgressView = new RecognitionProgressView(activity);


        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        recognitionProgressView.setLayoutParams(layoutParams);

        recognitionProgressView.setColors(colors);
        recognitionProgressView.setBarMaxHeightsInDp(heights);
        recognitionProgressView.setCircleRadiusInDp(5);
        recognitionProgressView.setSpacingInDp(2);
        recognitionProgressView.setIdleStateAmplitudeInDp(2);
        recognitionProgressView.setRotationRadiusInDp(10);
    }

    public void setSpeechReconigzer(SpeechRecognizer speechReconigzer) {
        recognitionProgressView.setSpeechRecognizer(speechReconigzer);
    }

    public RecognitionProgressView getView() {
        return recognitionProgressView;
    }

    public void play() {
        recognitionProgressView.play();
    }

    public void stop() {
        recognitionProgressView.stop();
    }
}
