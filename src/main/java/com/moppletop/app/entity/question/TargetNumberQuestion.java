package com.moppletop.app.entity.question;

public class TargetNumberQuestion extends QuizQuestion implements PostProcessQuestion {

    private final int target;

    public TargetNumberQuestion(String text, String image, int target) {
        super(QuizQuestionType.TARGET_NUMBER, text, image, String.valueOf(target));

        this.target = target;
    }

    @Override
    public void submitAnswer(String user, String answer) {
        try {
            Integer.parseInt(answer);
            answers.put(user, new QuizAnswer(answer, false));
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
                .ifPresent(entry -> entry.setValue(new QuizAnswer(entry.getValue().getText(), true)));
    }

}
