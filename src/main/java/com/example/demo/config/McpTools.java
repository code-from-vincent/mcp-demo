package com.example.demo.config;

import com.example.demo.greeting.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class McpTools {

    private final GreetingService greetingService;

    @McpTool(name = "greeting",
            description = "이름과 언어로 인사말을 생성합니다.")
    public String greeting(
            @McpToolParam(description = "이름") String name,
            @McpToolParam(description = "언어", required = false) String language
    ) {
        return greetingService.greet(name, language);
    }
}
