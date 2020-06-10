package com.moppletop.app.logic.command;

import com.moppletop.app.logic.Quiz;
import com.moppletop.app.logic.QuizManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RevealAnswerCommand extends QuizCommand {

    public RevealAnswerCommand(QuizManager manager) {
        super(manager, "answer");
    }

    @Override
    public void execute(Quiz quiz, String joinedArgs, String[] args) {
        if (quiz == null || quiz.getQuestion() == null) {
            return;
        }

        quiz.revealAnswers(Integer.parseInt(args[0]));
        log.info("Revealing answers and awarding points...");
    }
}
