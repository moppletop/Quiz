package com.moppletop.app.logic.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class QuizAnswer {

    final String text;
    QuizAnswerAccuracy accuracy;

}
