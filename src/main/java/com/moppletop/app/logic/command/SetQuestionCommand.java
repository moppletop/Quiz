package com.moppletop.app.logic.command;

import com.moppletop.app.entity.question.*;
import com.moppletop.app.logic.Quiz;
import com.moppletop.app.logic.QuizManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class SetQuestionCommand extends QuizCommand {

    public SetQuestionCommand(QuizManager manager) {
        super(manager, "question");
    }

    @Override
    public void execute(Quiz quiz, String joinedArgs, String[] args) {
        if (quiz == null || args.length < 2) {
            return;
        }

        QuizQuestionType type = QuizQuestionType.valueOf(args[0].toUpperCase());
        QuizQuestion question;
        String[] split = joinedArgs.split("\\|");
        String questionText = split[1];
        String correctAnswer = split[2];
        String image = null;

        if (args[1].startsWith("http") || args[1].startsWith("/")) {
            image = args[1];
        }

        switch (type) {
            case MULTIPLE_CHOICE:
                String[] options = new String[split.length - 2];
                System.arraycopy(split, 2, options, 0, options.length);
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

                question = new MultipleChoiceQuestion(questionText, image, options, answer);
                break;
            case FREE_TEXT:
                question = new FreeTextQuestion(questionText, image, correctAnswer);
                break;
            case AUDIO:
                if (image == null) {
                    throw new IllegalArgumentException("Must provide an audio path");
                } else {
                    question = new AudioQuestion(questionText, correctAnswer, image);
                }
                break;
            case TARGET_NUMBER:
                question = new TargetNumberQuestion(questionText, image, Integer.parseInt(correctAnswer));
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
