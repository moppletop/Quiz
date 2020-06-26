package com.moppletop.app.logic.question;

import com.moppletop.app.logic.answer.QuizAnswer;
import com.moppletop.app.logic.answer.QuizAnswerAccuracy;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString(callSuper = true)
@Slf4j
public class MultipleChoiceQuestion extends QuizQuestion {

    private final String[] options;

    public MultipleChoiceQuestion(int points, String text, String resource, String[] options, String answer) {
        super(QuizQuestionType.MULTIPLE_CHOICE, points, text, resource, answer);

        this.options = options;
    }

    @Override
    public void submitAnswer(String user, String[] answer) {
        try {
            int index = Integer.parseInt(answer[0]);
            String option = options[index];
            answers.put(user, new QuizAnswer(option, QuizAnswerAccuracy.fromBoolean(option.equals(this.correctAnswer))));
        } catch (NumberFormatException ex) {
            log.error("", ex);
        }
    }
}
