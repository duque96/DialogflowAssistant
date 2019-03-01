package dani.com.dialogflowassistance.logica.model;

import java.util.Calendar;
import java.util.Date;

public class Message {
    private User sender;
    private String message;
    private Date createdAt;

    public Message(User sender, String message) {
        this.createdAt = Calendar.getInstance().getTime();
        this.sender = sender;
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
