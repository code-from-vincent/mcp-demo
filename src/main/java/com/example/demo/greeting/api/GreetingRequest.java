package com.example.demo.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "인사 요청")
public class GreetingRequest {
    @Schema(description = "이름", example = "Vincent")
    @NotBlank
    @Size(max = 50)
    private String name;

    @Schema(description = "언어 코드 (ko, ja, en)", example = "ko")
    private String language;
}

