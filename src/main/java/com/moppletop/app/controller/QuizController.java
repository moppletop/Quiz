package com.moppletop.app.controller;

import com.moppletop.app.entity.QuizData;
import com.moppletop.app.entity.question.AudioQuestion;
import com.moppletop.app.entity.question.MultipleChoiceQuestion;
import com.moppletop.app.entity.question.QuizQuestion;
import com.moppletop.app.logic.Quiz;
import com.moppletop.app.logic.QuizManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizManager quizManager;
    private final TemplateEngine engine;

    @GetMapping("/data")
    public ResponseEntity<QuizData> data(HttpServletRequest request, HttpServletResponse response) {
        UserDetails user = ControllerUtil.getCurrentUser();
        quizManager.notifyUser(user);

        Quiz quiz = quizManager.getQuiz();
        String template;
        IWebContext context;
        Map<String, Object> variables;

        if (quiz == null) {
            return ResponseEntity.ok(new QuizData("", engine.process("pregame", new Context())));
        } else {
            QuizQuestion question = quiz.getQuestion();

            switch (quiz.getState()) {
                case PREGAME:
                    template = "pregame";
                    context = new WebContext(request, response, request.getServletContext());
                    break;
                case LIMBO:
                    template = "limbo";
                    context = new WebContext(request, response, request.getServletContext());
                    break;
                case INPUT:
                    variables = new HashMap<>();
                    String answer = quizManager.getAnswer(user);

                    if (answer == null) {
                        switch (quiz.getQuestion().getType()) {
                            case MULTIPLE_CHOICE:
                                MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) question;
                                template = "multiple-choice";
                                variables.put("options", multipleChoiceQuestion.getOptions());
                                break;
                            case FREE_TEXT:
                            case TARGET_NUMBER:
                                template = "free-text";
                                break;
                            case AUDIO:
                                AudioQuestion audioQuestion = (AudioQuestion) question;
                                template = "audio";
                                variables.put("audio", audioQuestion.getAudio());
                                break;
                            default:
                                return ResponseEntity.notFound().build();
                        }

                        variables.put("image", question.getImage());
                    } else {
                        template = "user-answer";
                        variables.put("answer", answer);
                        variables.put("numAnswered", question.getAnswers().size());
                        variables.put("numTotal", quiz.getScores().size());
                    }

                    variables.put("question", quiz.getQuestion().getText());
                    context = new WebContext(request, response, request.getServletContext(), request.getLocale(), variables);
                    break;
                case ANSWER:
                    template = "all-answers";
                    variables = new HashMap<>();

                    variables.put("question", question.getText());
                    variables.put("correctAnswer", question.getCorrectAnswer());
                    variables.put("answers", question.getAnswers());
                    variables.put("image", question.getImage());

                    context = new WebContext(request, response, request.getServletContext(), request.getLocale(), variables);
                    break;
                default:
                    return ResponseEntity.notFound().build();
            }
        }

        Map<String, Object> scores = quiz.getScores().entrySet().stream()
                .sorted((c1, c2) -> c2.getValue().compareTo(c1.getValue()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        QuizData quizData = new QuizData(
                engine.process("navbar", new WebContext(request, response, request.getServletContext(), request.getLocale(), Collections.singletonMap("scores", scores))),
                engine.process(template, context)
        );

        return ResponseEntity.ok(quizData);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submit(@RequestParam Map<String, String> params) {
        String answer = params.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("option"))
                .map(entry -> {
                    if (entry.getValue().length() > 30) {
                        return entry.getValue().substring(0, 30);
                    }

                    return entry.getValue();
                }).collect(Collectors.joining(" - "));

        if (!answer.isEmpty()) {
            quizManager.submit(ControllerUtil.getCurrentUser(), answer);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
