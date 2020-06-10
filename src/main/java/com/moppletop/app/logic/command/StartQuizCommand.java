package com.moppletop.app.logic.command;

import com.moppletop.app.logic.Quiz;
import com.moppletop.app.logic.QuizManager;

public class StartQuizCommand extends QuizCommand {

    public StartQuizCommand(QuizManager manager) {
        super(manager, "start");
    }

    @Override
    public void execute(Quiz quiz, String joinedArgs, String[] args) {
        manager.startQuiz();
    }
}
