package com.dani.dialogflowassistant.logica.model;

import com.google.cloud.dialogflow.v2.Intent;

import java.util.Date;

public interface Message {
    User getSender();

    Date getCreatedAt();

    Intent.Message.MessageCase getType();

    Intent.Message getMessage();
}
