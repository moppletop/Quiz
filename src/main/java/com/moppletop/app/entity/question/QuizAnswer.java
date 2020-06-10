package com.moppletop.app.entity.question;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuizAnswer {

    final String text;
    boolean correct;

}
