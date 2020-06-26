package com.moppletop.app.logic.question;

import com.moppletop.app.logic.answer.QuizAnswer;
import com.moppletop.app.logic.answer.QuizAnswerAccuracy;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

import java.util.regex.Pattern;

public class FreeTextQuestion extends QuizQuestion {

    private static final Pattern CLEAN_PATTERN = Pattern.compile("\\s");
    private static final NormalizedLevenshtein LEVENSHTEIN = new NormalizedLevenshtein();

    private static String clean(String text) {
        return CLEAN_PATTERN.matcher(text).replaceAll("").toLowerCase();
    }

    protected static QuizAnswerAccuracy getAccuracy(String correctAnswer, String userAnswer) {
        double dist = LEVENSHTEIN.distance(clean(correctAnswer), clean(userAnswer));

        if (dist < 0.1) {
            return QuizAnswerAccuracy.CORRECT;
        } else if (dist < 0.3) {
            return QuizAnswerAccuracy.PARTIALLY;
        } else {
            return QuizAnswerAccuracy.INCORRECT;
        }
    }

    public FreeTextQuestion(int points, String text, String resource, String correctAnswer) {
        this(QuizQuestionType.FREE_TEXT, points, text, resource, correctAnswer);
    }

    public FreeTextQuestion(QuizQuestionType questionType, int points, String text, String resource, String correctAnswer) {
        super(questionType, points, text, resource, correctAnswer);
    }

    @Override
    public void submitAnswer(String user, String[] answer) {
        answers.put(user, new QuizAnswer(answer[0], getAccuracy(correctAnswer, answer[0])));
    }

}
