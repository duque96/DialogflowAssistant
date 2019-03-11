package com.dani.dialogflowassistant.logica.responseType;

import com.dani.dialogflowassistant.vista.MainActivity;
import com.dani.dialogflowassistant.logica.model.Message;
import com.dani.dialogflowassistant.logica.model.TextMessage;
import com.dani.dialogflowassistant.logica.model.User;
import com.dani.dialogflowassistant.logica.speech.Speaker;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryResult;

public class TextResponse implements ResponseType {
    private Speaker textToSpeech;
    private MainActivity activity;

    TextResponse(MainActivity activity) {
        this.activity = activity;
        this.textToSpeech = new Speaker();
    }

    @Override
    public Message execute(QueryResult queryResult, User user, Intent.Message message) {
        textToSpeech.speak(message.getText().getText(0), activity);

        return new TextMessage(user, message);
    }
}
