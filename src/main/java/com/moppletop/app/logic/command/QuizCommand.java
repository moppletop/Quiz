package com.moppletop.app.logic.command;

import com.moppletop.app.logic.Quiz;
import com.moppletop.app.logic.QuizManager;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@RequiredArgsConstructor
public abstract class QuizCommand implements Predicate<String> {

    protected final QuizManager manager;
    private final String command;

    public abstract void execute(Quiz quiz, String joinedArgs, String[] args);

    @Override
    public boolean test(String command) {
        return this.command.equals(command);
    }
}
