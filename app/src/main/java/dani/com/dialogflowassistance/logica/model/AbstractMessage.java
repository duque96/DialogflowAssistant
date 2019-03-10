package dani.com.dialogflowassistance.logica.model;

import com.google.cloud.dialogflow.v2.Intent;

import java.util.Date;

public abstract class AbstractMessage implements Message {
    User sender;
    Date createdAt;

    @Override
    public User getSender() {
        return sender;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Intent.Message getMessage() {
        return null;
    }
}
