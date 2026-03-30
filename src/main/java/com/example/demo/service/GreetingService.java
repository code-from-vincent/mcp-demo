package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GreetingService {

    public String greet(String name, String language) {
        if (!StringUtils.hasText(name)) {
            name = "Stranger";
        }

        String normalizedLanguage = language == null ? "en" : language.trim().toLowerCase();
        return switch (normalizedLanguage) {
            case "ko" -> "안녕하세요, " + name + "!";
            case "ja" -> "こんにちは, " + name + "!";
            default -> "Hello, " + name + "!";
        };
    }
}

