package dani.com.dialogflowassistance.logica.model;

import com.google.cloud.dialogflow.v2.Intent;

import java.util.Calendar;

public class TextMessage extends AbstractMessage {
    private String message;

    public TextMessage(User sender, String message) {
        this.createdAt = Calendar.getInstance().getTime();
        this.sender = sender;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Intent.Message.MessageCase getType() {
        return Intent.Message.MessageCase.TEXT;
    }
}
