package com.moppletop.app.logic.command;

import com.moppletop.app.logic.Quiz;
import com.moppletop.app.logic.QuizManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScoreCommand extends QuizCommand {

    public ScoreCommand(QuizManager manager) {
        super(manager, "score");
    }

    @Override
    public void execute(Quiz quiz, QuizCommandAnalysis analysis) {
        if (quiz == null) {
            return;
        }

        String user = analysis.getRequiredFlag("user");
        String op = analysis.getRequiredFlag("op");
        int amount = Integer.parseInt(analysis.getRequiredFlag("amount"));
        int score = quiz.getScores().getOrDefault(user, 0);

        switch (op.toLowerCase()) {
            case "add":
                score += amount;
                break;
            case "sub":
                score -= amount;
                break;
            case "set":
                score = amount;
                break;
            default:
                throw new IllegalArgumentException("The op flag must be either add, sub or set!");
        }

        quiz.setScore(user, score);
        log.info("Updated points for {} to {}", user, score);
    }
}
