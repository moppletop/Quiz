package com.moppletop.autoconfigure;

import com.moppletop.app.logic.QuizManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = QuizManager.class)
public class QuizLogicAutoConfiguration {
}
