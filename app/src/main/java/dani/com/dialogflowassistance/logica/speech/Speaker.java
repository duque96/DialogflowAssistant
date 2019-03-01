package dani.com.dialogflowassistance.logica.speech;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Speaker {
    private TextToSpeech tts;
    private TextToSpeech.OnInitListener listener;

    @Deprecated
    public void speak(final String text, Context context) {
        tts = new TextToSpeech(context, createListener(text), "com.google.android.tts");
    }

    private TextToSpeech.OnInitListener createListener(final String text) {
        return new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(new Locale("es", "ES"));
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    } else {
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }
        };
    }
}
