package dani.com.dialogflowassistance.logica.responseType;

import com.google.cloud.dialogflow.v2.QueryResult;

import java.util.List;

import dani.com.dialogflowassistance.logica.model.Message;
import dani.com.dialogflowassistance.logica.model.User;

public interface ResponseType {
    void execute(QueryResult queryResult, List<Message> messageList, User user);
}
