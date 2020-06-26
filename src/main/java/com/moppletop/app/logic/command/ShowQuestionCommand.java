package com.moppletop.app.logic.command;

import com.moppletop.app.logic.question.*;
import com.moppletop.app.logic.Quiz;
import com.moppletop.app.logic.QuizManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class ShowQuestionCommand extends QuizCommand {

    public ShowQuestionCommand(QuizManager manager) {
        super(manager, "show");
    }

    @Override
    public void execute(Quiz quiz, QuizCommandAnalysis analysis) {
        if (quiz == null) {
            return;
        }

        QuizQuestionType type = QuizQuestionType.valueOf(analysis.getRequiredFlag("type").toUpperCase());
        int points = Integer.parseInt(analysis.getRequiredFlag("points"));
        QuizQuestion question;
        String questionText = analysis.getRequiredFlag("question");
        String resourcePath = analysis.getFlag("resource");

        switch (type) {
            case MULTIPLE_CHOICE:
                String[] options = analysis.getRequiredFlag("options").split("\\|");
                String answer = options[0];
                boolean shuffle = true;

                String o;
                for (int i = 0; i < options.length; i++) {
                    o = options[i];

                    if (o.charAt(0) == '*') {
                        answer = o.substring(1);
                        options[i] = answer;
                        shuffle = false;
                    }
                }

                if (shuffle) {
                    shuffleArray(options);
                }

                question = new MultipleChoiceQuestion(points, questionText, resourcePath, options, answer);
                break;
            case FREE_TEXT:
                question = new FreeTextQuestion(points, questionText, resourcePath, analysis.getRequiredFlag("answer"));
                break;
            case AUDIO:
                if (resourcePath == null) {
                    throw new IllegalArgumentException("Must provide an audio path");
                } else {
                    question = new AudioQuestion(points, questionText, analysis.getRequiredFlag("title"), analysis.getRequiredFlag("author"), resourcePath);
                }
                break;
            case TARGET_NUMBER:
                question = new TargetNumberQuestion(points, questionText, resourcePath, Integer.parseInt(analysis.getRequiredFlag("answer")));
                break;
            default:
                return;
        }

        log.info("Setting question to {}", question);
        quiz.setQuestion(question);
    }

    private static void shuffleArray(Object[] ar) {
        Random random = ThreadLocalRandom.current();

        for (int i = ar.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            Object a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
