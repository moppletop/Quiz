package com.moppletop.app.logic.question;

import com.moppletop.app.logic.answer.QuizAnswer;
import com.moppletop.app.logic.answer.QuizAnswerAccuracy;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class AudioQuestion extends FreeTextQuestion {

    private final String audio;
    private final String title;
    private final String author;

    public AudioQuestion(int points, String text, String title, String author, String audio) {
        super(QuizQuestionType.AUDIO, points, text, null, title + " - " + author);

        this.audio = audio;
        this.title = title;
        this.author = author;
    }

    @Override
    public void submitAnswer(String user, String[] answer) {
        QuizAnswerAccuracy titleAccuracy = getAccuracy(title, answer[0]);
        QuizAnswerAccuracy authorAccuracy = getAccuracy(author, answer[1]);
        QuizAnswerAccuracy totalAccuracy;

        if (titleAccuracy != QuizAnswerAccuracy.INCORRECT && authorAccuracy != QuizAnswerAccuracy.INCORRECT) {
            totalAccuracy = QuizAnswerAccuracy.CORRECT;
        } else if (titleAccuracy != QuizAnswerAccuracy.INCORRECT || authorAccuracy != QuizAnswerAccuracy.INCORRECT) {
            totalAccuracy = QuizAnswerAccuracy.PARTIALLY;
        } else {
            totalAccuracy = QuizAnswerAccuracy.INCORRECT;
        }

        answers.put(user, new QuizAnswer(answer[0] + " - " + answer[1], totalAccuracy));
    }
}
