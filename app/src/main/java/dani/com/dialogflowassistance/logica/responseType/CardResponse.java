package dani.com.dialogflowassistance.logica.responseType;

import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryResult;

import java.util.List;

import dani.com.dialogflowassistance.logica.model.CardMessage;
import dani.com.dialogflowassistance.logica.model.Message;
import dani.com.dialogflowassistance.logica.model.User;

public class CardResponse implements ResponseType {

    @Override
    public void execute(QueryResult queryResult, List<Message> messageList, User user) {
        Intent.Message message = queryResult.getFulfillmentMessages(0);
        messageList.add(new CardMessage(user, message));
    }
}
