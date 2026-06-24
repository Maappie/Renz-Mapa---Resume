package com.renzmapa.resume_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("api/hello")
    public String sayHello() {
        return "Hi, I am Renz Mapa. I am practicing the Java Springboot framework!";
    }
}