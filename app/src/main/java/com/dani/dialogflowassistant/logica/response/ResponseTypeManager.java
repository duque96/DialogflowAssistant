package com.dani.dialogflowassistant.logica.response;

import com.dani.dialogflowassistant.logica.model.User;
import com.dani.dialogflowassistant.vista.MainActivity;
import com.google.cloud.dialogflow.v2.Context;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryResult;

public class ResponseTypeManager {

    private ResponseType responseType;
    private QueryResult queryResult;
    private MainActivity activity;
    private User user;

    public ResponseTypeManager(QueryResult queryResult, MainActivity activity, User user) {
        this.queryResult = queryResult;
        this.activity = activity;
        this.user = user;
    }

    public void createResponseMessage() {
        for (int i = 0; i < queryResult.getFulfillmentMessagesCount(); i++) {
            Intent.Message message = queryResult.getFulfillmentMessages(i);
            Context context = null;
            if (queryResult.getOutputContextsCount() > 0)
                context = queryResult.getOutputContexts(0);

            switch (message.getMessageCase()) {
                case TEXT:
                    responseType = new TextResponse(activity);
                    break;
                case CARD:
                    responseType = new CardResponse();
                    break;
                case SUGGESTIONS:
                    responseType = new SuggestionResponse();
                    break;
                default:
                    break;
            }

            activity.addMessages(responseType.execute(queryResult, user, message,
                    context));
        }
    }
}
