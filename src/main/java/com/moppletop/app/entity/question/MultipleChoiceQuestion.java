package com.moppletop.app.entity.question;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString(callSuper = true)
@Slf4j
public class MultipleChoiceQuestion extends QuizQuestion {

    private final String[] options;

    public MultipleChoiceQuestion(String text, String image, String[] options, String answer) {
        super(QuizQuestionType.MULTIPLE_CHOICE, text, image, answer);

        this.options = options;
    }

    @Override
    public void submitAnswer(String user, String answer) {
        try {
            int index = Integer.parseInt(answer);
            String option = options[index];
            answers.put(user, new QuizAnswer(option, option.equals(this.correctAnswer)));
        } catch (NumberFormatException ex) {
            log.error("", ex);
        }
    }
}
