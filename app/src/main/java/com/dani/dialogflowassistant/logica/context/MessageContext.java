package com.dani.dialogflowassistant.logica.context;

public class MessageContext {
    private static final String ANSWER_CONTEXT = "answer_context";

    public static boolean isQuestion(String context) {
        return !context.equals(ANSWER_CONTEXT);
    }

    public static boolean isAnswer(String context) {
        return context.equals(ANSWER_CONTEXT);
    }
}
