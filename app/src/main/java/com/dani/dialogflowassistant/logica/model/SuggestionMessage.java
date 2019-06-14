package com.dani.dialogflowassistant.logica.model;

import com.google.cloud.dialogflow.v2.Context;
import com.google.cloud.dialogflow.v2.Intent;

import java.util.Calendar;

public class SuggestionMessage extends AbstractMessage {
    private Intent.Message message;
    private Context context;

    public SuggestionMessage(User sender, Intent.Message message, Context context) {
        this.createdAt = Calendar.getInstance().getTime();
        this.sender = sender;
        this.message = message;
        this.context = context;
    }

    public Intent.Message getMessage() {
        return message;
    }

    @Override
    public Intent.Message.MessageCase getType() {
        return Intent.Message.MessageCase.SUGGESTIONS;
    }

    @Override
    public Context getContext() {
        return context;
    }
}
