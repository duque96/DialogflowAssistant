package dani.com.dialogflowassistance.logica.responseType;

import android.content.Context;

import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryResult;

import java.util.List;

import dani.com.dialogflowassistance.logica.model.Message;
import dani.com.dialogflowassistance.logica.model.TextMessage;
import dani.com.dialogflowassistance.logica.model.User;
import dani.com.dialogflowassistance.logica.speech.Speaker;

public class TextResponse implements ResponseType {
    private Speaker textToSpeech;
    private Context context;

    public TextResponse(Context context) {
        this.context = context;
        this.textToSpeech = new Speaker();
    }

    @Override
    public void execute(QueryResult queryResult, List<Message> messageList, User user) {
        Intent.Message message = queryResult.getFulfillmentMessages(0);
        messageList.add(new TextMessage(user, message.getText().getText(0)));
        textToSpeech.speak(message.getText().getText(0), context);
    }
}
