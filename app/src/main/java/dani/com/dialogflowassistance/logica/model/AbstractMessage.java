package dani.com.dialogflowassistance.logica.model;

import java.util.Date;

public abstract class AbstractMessage implements Message {
    protected User sender;
    protected Date createdAt;

    @Override
    public User getSender() {
        return sender;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSubtitle() {
        return null;
    }

    @Override
    public String getButton() {
        return null;
    }

    @Override
    public String getImageURL() {
        return null;
    }

    @Override
    public String getApp() {
        return null;
    }

    @Override
    public String getUri() {
        return null;
    }
}
