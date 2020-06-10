package com.moppletop.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class QuizData {

    String htmlNav;
    String htmlBody;

    // Used by the web page to check if the state has changed
    @SuppressWarnings("unused")
    public int getHashCode() {
        return hashCode();
    }

}
