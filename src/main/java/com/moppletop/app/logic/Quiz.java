package com.moppletop.app.logic;

import com.moppletop.app.entity.QuizState;
import com.moppletop.app.entity.question.PostProcessQuestion;
import com.moppletop.app.entity.question.QuizAnswer;
import com.moppletop.app.entity.question.QuizQuestion;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Quiz {

    private final Map<String, Integer> scores;

    private QuizState state;
    private QuizQuestion question;

    public Quiz() {
        this.scores = new HashMap<>();
        this.state = QuizState.LIMBO;
    }

    void addUser(String user) {
        scores.putIfAbsent(user, 0);
    }

    void submit(String user, String answer) {
        if (question != null) {
            question.submitAnswer(user, answer);
        }
    }

    public void setScore(String user, int score) {
        if (score < 0) {
            scores.remove(user);
        } else {
            scores.put(user, score);
        }
    }

    public Map<String, Integer> getScores() {
        Map<String, Integer> scores = new HashMap<>(this.scores);
        scores.remove(QuizManager.ADMIN_USER);
        return scores;
    }

    public void setQuestion(QuizQuestion question) {
        this.state = QuizState.INPUT;
        this.question = question;
    }

    public void revealAnswers(int points) {
        if (question instanceof PostProcessQuestion) {
            ((PostProcessQuestion) question).process();
        }

        this.state = QuizState.ANSWER;
        Map<String, QuizAnswer> answers = question.getAnswers();

        scores.forEach((user, score) -> {
            QuizAnswer answer = answers.get(user);

            if (answer == null) {
                question.setAnswer(user, new QuizAnswer(QuizManager.NO_ANSWER, false));
            } else if (answer.isCorrect()){
                scores.put(user, scores.get(user) + points);
            }
        });
    }

}
