package com.moppletop.autoconfigure;

import com.moppletop.app.controller.PagesController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PagesController.class)
public class QuizControllerAutoConfiguration {
}
