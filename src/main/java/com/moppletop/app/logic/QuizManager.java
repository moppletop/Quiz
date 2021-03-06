package com.moppletop.app.logic;

import com.moppletop.app.logic.answer.QuizAnswer;
import com.moppletop.app.logic.command.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Scanner;

@Component
@Slf4j
public class QuizManager {

    public static final String ADMIN_USER = "admin";
    public static final String NO_ANSWER = "No Answer";

    private final QuizCommand[] commands = {
            new StartQuizCommand(this),
            new ScoreCommand(this),
            new ShowQuestionCommand(this),
            new RevealAnswerCommand(this),
    };

    private Quiz quiz;

    public void startQuiz() {
        log.info("Starting a new quiz...");
        this.quiz = new Quiz();
    }

    public void notifyUser(UserDetails user) {
        if (quiz != null) {
            quiz.addUser(user.getUsername());
        }
    }

    public void submit(UserDetails user, String[] answers) {
        if (quiz != null) {
            quiz.submit(user.getUsername(), answers);
        }
    }

    public String getAnswer(UserDetails user) {
        if (quiz == null || quiz.getQuestion() == null) {
            return null;
        } else if (user.getUsername().equals(ADMIN_USER)) {
            return ADMIN_USER;
        }

        QuizAnswer answer = quiz.getQuestion().getAnswers().get(user.getUsername());
        return answer == null ? null : answer.getText();
    }

    public Quiz getQuiz() {
        return quiz;
    }

    @PostConstruct
    public void inputLoop() {
        Thread thread = new Thread(this::readCommands);
        thread.setName("input");
        thread.start();
    }

    private void readCommands() {
        Scanner scanner = new Scanner(System.in);
        String input;

        log.info("Ready to take input...");

        inputLoop:
        while ((input = scanner.nextLine()) != null) {
            input = input.trim();

            for (QuizCommand command : commands) {
                if (command.test(input)) {
                    try {
                        command.execute(quiz, new QuizCommandAnalysis(input));
                    } catch (Throwable ex) {
                        log.warn("Error executing command: {} -> {}", ex.getClass().getSimpleName(), ex.getMessage());
                    }

                    continue inputLoop;
                }
            }

            log.info("Unknown command");
        }
    }

}
