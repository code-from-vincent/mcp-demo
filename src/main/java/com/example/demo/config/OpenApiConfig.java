package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI demoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Demo API")
                        .version("v1")
                        .description("Spring Boot + OpenAPI + JSON Schema + MCP Server 예제"));
    }
}