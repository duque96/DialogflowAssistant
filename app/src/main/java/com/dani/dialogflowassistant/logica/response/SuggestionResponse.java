package com.dani.dialogflowassistant.logica.response;

import com.dani.dialogflowassistant.logica.model.Message;
import com.dani.dialogflowassistant.logica.model.SuggestionMessage;
import com.dani.dialogflowassistant.logica.model.User;
import com.google.cloud.dialogflow.v2.Context;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryResult;

public class SuggestionResponse implements ResponseType {
    @Override
    public Message execute(QueryResult queryResult, User user, Intent.Message message, Context context) {
        return new SuggestionMessage(user, message, context);
    }
}
