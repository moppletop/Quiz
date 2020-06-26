package com.moppletop.app.logic.answer;

public enum QuizAnswerAccuracy {

    CORRECT,
    PARTIALLY,
    INCORRECT;

    public static QuizAnswerAccuracy fromBoolean(boolean correct) {
        return correct ? CORRECT : INCORRECT;
    }

}
