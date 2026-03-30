package com.example.demo.greeting.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "인사 응답")
public class GreetingResponse {
    @Schema(description = "생성된 메시지", example = "안녕하세요, Vincent!")
    private String message;
}

