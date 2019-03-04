package dani.com.dialogflowassistance.logica.responseType;

import android.content.Context;

import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryResult;

import java.util.List;

import dani.com.dialogflowassistance.logica.model.CardMessage;
import dani.com.dialogflowassistance.logica.model.Message;
import dani.com.dialogflowassistance.logica.model.TextMessage;
import dani.com.dialogflowassistance.logica.model.User;

public class CardResponse implements ResponseType {
    private Context context;

    @Override
    public void execute(QueryResult queryResult, List<Message> messageList, User user) {
        Intent.Message message = queryResult.getFulfillmentMessages(0);
        messageList.add(new CardMessage(user, message.getCard().getTitle(),
                message.getCard().getSubtitle(), message.getCard().getButtons(0).getText(),
                message.getCard().getImageUri(), queryResult.getFulfillmentText(),
                message.getCard().getButtons(0).getPostback()));
    }
}
