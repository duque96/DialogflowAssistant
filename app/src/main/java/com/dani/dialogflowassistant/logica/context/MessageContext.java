package com.dani.dialogflowassistant.logica.context;

public class MessageContext {
    private static final String ANSWER_CONTEXT = "answer_context";

    public static boolean isQuestion(String context) {
        if (!context.equals(ANSWER_CONTEXT))
            return true;
        return false;
    }

    public static boolean isAnswer(String context) {
        if (context.equals(ANSWER_CONTEXT))
            return true;
        return false;
    }
}
