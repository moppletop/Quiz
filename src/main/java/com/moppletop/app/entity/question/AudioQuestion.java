package com.moppletop.app.entity.question;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class AudioQuestion extends FreeTextQuestion {

    private final String audio;

    public AudioQuestion(String text, String correctAnswer, String audio) {
        super(QuizQuestionType.AUDIO, text, null, correctAnswer);

        this.audio = audio;
    }

}
