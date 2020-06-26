package com.moppletop.app.logic.question;

import com.moppletop.app.logic.answer.QuizAnswer;
import com.moppletop.app.logic.answer.QuizAnswerAccuracy;

public class TargetNumberQuestion extends QuizQuestion implements PostProcessQuestion {

    private final int target;

    public TargetNumberQuestion(int points, String text, String resource, int target) {
        super(QuizQuestionType.TARGET_NUMBER, points, text, resource, String.valueOf(target));

        this.target = target;
    }

    @Override
    public void submitAnswer(String user, String[] answer) {
        try {
            Integer.parseInt(answer[0]);
            answers.put(user, new QuizAnswer(answer[0]));
        } catch (NumberFormatException ex) {
            // silently fail...
        }
    }

    @Override
    public void process() {
        answers.entrySet().stream()
                .min((o1, o2) -> {
                    String aT = o1.getValue().getText(), bT = o2.getValue().getText();
                    return Integer.compare(Math.abs(target - Integer.parseInt(aT)), Math.abs(target - Integer.parseInt(bT)));
                })
                .ifPresent(entry -> entry.setValue(new QuizAnswer(entry.getValue().getText(), QuizAnswerAccuracy.CORRECT)));
    }

}
