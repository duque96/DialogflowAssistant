package com.dani.dialogflowassistant.logica.context;

public class MessageContext {
    private static final String QUESTION_CONTEXT = "question_context";
    private static final String ANSWER_CONTEXT = "answer_context";
    private static final String NO_MATCHED_QUERY_CONTEXT = "no_matched_query_context";
    private static final String SUGGESTIONS_CONTEXT = "suggestions_context";

    public static boolean isQuestion(String context) {
        if (context.equals(QUESTION_CONTEXT) || context.equals(NO_MATCHED_QUERY_CONTEXT))
            return true;
        return false;
    }

    public static boolean isAnswer(String context) {
        if (context.equals(ANSWER_CONTEXT) || context.equals(SUGGESTIONS_CONTEXT))
            return true;
        return false;
    }
}
