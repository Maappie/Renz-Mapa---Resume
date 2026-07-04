package com.renzmapa.resume_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Automatically prefix "/api" to all REST Controllers (which return JSON data)
        configurer.addPathPrefix("/api", 
            HandlerTypePredicate.forAnnotation(RestController.class)
        );
    }
}
