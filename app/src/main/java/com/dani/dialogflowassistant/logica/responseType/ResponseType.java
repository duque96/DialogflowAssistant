package com.dani.dialogflowassistant.logica.responseType;

import com.dani.dialogflowassistant.logica.model.Message;
import com.dani.dialogflowassistant.logica.model.User;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryResult;

import java.util.List;

public interface ResponseType {
    Message execute(QueryResult queryResult, User user, Intent.Message message);
}
