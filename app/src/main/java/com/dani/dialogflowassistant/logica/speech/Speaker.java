package com.dani.dialogflowassistant.logica.speech;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.dani.dialogflowassistant.logica.context.MessageContext;
import com.dani.dialogflowassistant.logica.util.ViewGroupUtils;
import com.dani.dialogflowassistant.vista.MainActivity;

import java.util.Locale;


public class Speaker {
    private TextToSpeech tts;
    private MainActivity activity;

    public void speak(String text, final MainActivity activity) {
        this.activity = activity;
        tts = new TextToSpeech(activity, createListener(text), "com.google.android.tts");
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
                        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onStart(String utteranceId) {
                                Log.i("StartSpeaker", "The speaker starts to play");
                            }

                            @Override
                            public void onDone(String utteranceId) {
                                Handler handler = new Handler(activity.getMainLooper());
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (activity.getMessageList().get(activity.getMessageList().size() - 1).getContext() != null) {
                                            String[] parts =
                                                    activity.getMessageList().get(activity.getMessageList().size() - 1).getContext().getName().split("/");
                                            String context = parts[6];
                                            if (MessageContext.isQuestion(context) && !tts.isSpeaking()) {
                                                activity.getSpeechRecognitionView().stop();
                                                activity.getSpeechRecognitionView().play();
                                                activity.activarMicrofono(null);
                                            } else if (MessageContext.isAnswer(context) && !tts.isSpeaking())
                                                ViewGroupUtils.speechToMicView();
                                        } else {
                                            ViewGroupUtils.speechToMicView();
                                        }
                                    }
                                };
                                handler.post(runnable);
                            }

                            @Override
                            public void onError(String utteranceId) {
                                Log.e("SpeakerError", "An error has occured while the speaker was playing");
                            }
                        });

                        tts.speak(text, TextToSpeech.QUEUE_ADD, null,
                                TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
                    }
                } else
                    Log.e("SpeakerError", "Initilization Failed!");
            }
        };
    }

    public void stopSpeaking() {
        if (tts.isSpeaking()){
            tts.stop();
        }
    }

    public void shutDown() {
        tts.shutdown();
    }
}
