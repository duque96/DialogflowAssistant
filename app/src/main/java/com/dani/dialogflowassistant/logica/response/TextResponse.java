package com.dani.dialogflowassistant.logica.response;

import com.dani.dialogflowassistant.logica.model.Message;
import com.dani.dialogflowassistant.logica.model.TextMessage;
import com.dani.dialogflowassistant.logica.model.User;
import com.dani.dialogflowassistant.vista.MainActivity;
import com.google.cloud.dialogflow.v2.Context;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryResult;

public class TextResponse implements ResponseType {

    private MainActivity activity;

    TextResponse(MainActivity activity) {
        this.activity = activity;

    }

    @Override
    public Message execute(QueryResult queryResult, User user, Intent.Message message, Context context) {
        activity.getSpeaker().speak(message.getText().getText(0), activity);

        return new TextMessage(user, message, context);
    }
}
