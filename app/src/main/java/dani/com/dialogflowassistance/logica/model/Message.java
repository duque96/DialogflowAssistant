package dani.com.dialogflowassistance.logica.model;

import com.google.cloud.dialogflow.v2.Intent;

import java.util.Date;

public interface Message {
    User getSender();

    Date getCreatedAt();

    Intent.Message.MessageCase getType();

    String getMessage();

    String getTitle();

    String getSubtitle();

    String getButton();

    String getImageURL();

    String getApp();

    String getUri();
}
