package com.example.demo.api;

import com.example.demo.service.GreetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "Greeting API", description = "간단한 인사 API")
public class GreetingController {

    private final GreetingService greetingService;

    @PostMapping("/greet")
    @Operation(summary = "인사말 생성", description = "이름과 언어를 입력받아 인사말을 생성합니다.")
    public GreetingResponse greet(@Valid @RequestBody GreetingRequest request) {
        return new GreetingResponse(greetingService.greet(request.getName(), request.getLanguage()));
    }
}
