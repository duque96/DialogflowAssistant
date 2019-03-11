package com.dani.dialogflowassistant.logica.util;

import android.view.View;
import android.view.ViewGroup;

import com.dani.dialogflowassistant.vista.MainActivity;

public class ViewGroupUtils {
    private static MainActivity activity;

    private static ViewGroup getParent(View view) {
        return (ViewGroup) view.getParent();
    }

    private static void removeView(View view) {
        ViewGroup parent = getParent(view);
        if (parent != null) {
            parent.removeView(view);
        }
    }

    public static void replaceView(View currentView, View newView) {
        ViewGroup parent = getParent(currentView);
        if (parent == null) {
            return;
        }

        final int index = parent.indexOfChild(currentView);
        removeView(currentView);
        removeView(newView);
        parent.addView(newView, index);
    }

    public static void speechToMicView() {
        activity.getSpeechRecognitionView().stop();
        ViewGroupUtils.replaceView(activity.getSpeechRecognitionView(),
                activity.getRecordingButton());
    }

    public static void micToSpeechView() {
        ViewGroupUtils.replaceView(activity.getRecordingButton(),
                activity.getSpeechRecognitionView());
        activity.getSpeechRecognitionView().play();
    }

    public static void setActivity(MainActivity a) {
        activity = a;
    }
}