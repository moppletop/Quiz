package com.moppletop.app.entity.question;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

import java.util.regex.Pattern;

public class FreeTextQuestion extends QuizQuestion {

    private static final Pattern CLEAN_PATTERN = Pattern.compile("\\s");
    private static final NormalizedLevenshtein LEVENSHTEIN = new NormalizedLevenshtein();

    private static String clean(String text) {
        return CLEAN_PATTERN.matcher(text).replaceAll("").toLowerCase();
    }

    public FreeTextQuestion(String text, String image, String correctAnswer) {
        this(QuizQuestionType.FREE_TEXT, text, image, correctAnswer);
    }

    public FreeTextQuestion(QuizQuestionType questionType, String text, String image, String correctAnswer) {
        super(questionType, text, image, correctAnswer);
    }

    @Override
    public void submitAnswer(String user, String answer) {
        answers.put(user, new QuizAnswer(answer, LEVENSHTEIN.distance(clean(correctAnswer), clean(answer)) < 0.1));
    }

}
