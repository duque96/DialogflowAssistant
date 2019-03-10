package dani.com.dialogflowassistance.logica.responseType;

import android.content.Context;

import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryResult;

import java.util.List;

import dani.com.dialogflowassistance.logica.model.Message;
import dani.com.dialogflowassistance.logica.model.User;

public class ResponseTypeManager {

    private ResponseType responseType;
    private QueryResult queryResult;
    private Context context;
    private List<Message> messageList;
    private User user;

    public ResponseTypeManager(QueryResult queryResult, Context context,
                               List<Message> messageList, User user) {
        this.queryResult = queryResult;
        this.context = context;
        this.messageList = messageList;
        this.user = user;
    }

    public void createResponseMessage() {
        for (int i = 0; i < queryResult.getFulfillmentMessagesCount(); i++) {
            Intent.Message.MessageCase messageCase = queryResult.getFulfillmentMessages(i)
                    .getMessageCase();

            switch (messageCase) {
                case TEXT:
                    responseType = new TextResponse(context);
                    break;
                case CARD:
                    responseType = new CardResponse();
                default:
                    break;
            }

            responseType.execute(queryResult, messageList, user);
        }
    }
}
