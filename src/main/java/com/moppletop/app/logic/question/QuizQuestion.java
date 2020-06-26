package com.moppletop.app.logic.question;

import com.moppletop.app.logic.QuizManager;
import com.moppletop.app.logic.answer.QuizAnswer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@ToString(exclude = "answers")
public abstract class QuizQuestion {

    final QuizQuestionType type;
    final int points;
    final String text;
    final String resource;
    final String correctAnswer;
    final Map<String, QuizAnswer> answers = new HashMap<>();

    public abstract void submitAnswer(String user, String[] answer);

    public void setAnswer(String user, QuizAnswer answer) {
        answers.put(user, answer);
    }

    public Map<String, QuizAnswer> getAnswers() {
        Map<String, QuizAnswer> answers = new HashMap<>(this.answers);
        answers.remove(QuizManager.ADMIN_USER);
        return answers;
    }

}
