package com.moppletop.app.logic.command;

import com.moppletop.app.logic.Quiz;
import com.moppletop.app.logic.QuizManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetPointsCommand extends QuizCommand {

    public SetPointsCommand(QuizManager manager) {
        super(manager, "points");
    }

    @Override
    public void execute(Quiz quiz, String joinedArgs, String[] args) {
        if (quiz == null || args.length < 2) {
            return;
        }

        quiz.setScore(args[0], Integer.parseInt(args[1]));
        log.info("Updated points for {} to {}", args[0], args[1]);
    }
}
