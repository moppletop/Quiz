package com.moppletop.app.controller;

import com.moppletop.app.logic.QuizManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class PagesController {

    private final QuizManager quizManager;

    @GetMapping("/")
    public String home(Model model) {
        UserDetails user = ControllerUtil.getCurrentUser();

        model.addAttribute("title", "Quiz");
        model.addAttribute("answer", quizManager.getAnswer(user));

        return "home";
    }

}
