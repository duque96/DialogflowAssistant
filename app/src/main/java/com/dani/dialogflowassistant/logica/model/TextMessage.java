package com.dani.dialogflowassistant.logica.model;

import com.google.cloud.dialogflow.v2.Intent;

import java.util.Calendar;

public class TextMessage extends AbstractMessage {
    private Intent.Message message;

    public TextMessage(User sender, Intent.Message message) {
        this.createdAt = Calendar.getInstance().getTime();
        this.sender = sender;
        this.message = message;
    }

    public Intent.Message getMessage() {
        return message;
    }

    public Intent.Message.MessageCase getType() {
        return Intent.Message.MessageCase.TEXT;
    }
}
