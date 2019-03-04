package dani.com.dialogflowassistance.logica.model;

import com.google.cloud.dialogflow.v2.Intent;

import java.util.Calendar;

public class CardMessage extends AbstractMessage {
    private String title;
    private String subtitle;
    private String button;
    private String imageURL;
    private String app;
    private String uri;

    public CardMessage(User sender, String title, String subtitle, String button, String imageURL
            , String app, String uri) {
        this.createdAt = Calendar.getInstance().getTime();
        this.sender = sender;
        this.title = title;
        this.subtitle = subtitle;
        this.button = button;
        this.imageURL = imageURL;
        this.app = app;
        this.uri = uri;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public String getButton() {
        return button;
    }

    @Override
    public String getImageURL() {
        return imageURL;
    }

    public Intent.Message.MessageCase getType() {
        return Intent.Message.MessageCase.CARD;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public String getApp() {
        return app;
    }
}
