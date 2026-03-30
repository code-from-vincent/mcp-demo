package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiAndMcpIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void openApiDocsAreExposed() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/api/greet")));
    }

    @Test
    void restApiValidatesJsonSchema() throws Exception {
        mockMvc.perform(post("/api/greet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Copilot\",\"language\":\"en\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, Copilot!"));

        mockMvc.perform(post("/api/greet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"language\":\"en\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void mcpEndpointsReturnToolListAndToolResult() throws Exception {
        MvcResult initResult = mockMvc.perform(post("/mcp")
                        .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_EVENT_STREAM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "jsonrpc": "2.0",
                                  "id": 1,
                                  "method": "initialize",
                                  "params": {
                                    "protocolVersion": "2025-03-26",
                                    "capabilities": {},
                                    "clientInfo": {
                                      "name": "mockmvc-test-client",
                                      "version": "1.0.0"
                                    }
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String sessionId = initResult.getResponse().getHeader("Mcp-Session-Id");

        mockMvc.perform(post("/mcp")
                        .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_EVENT_STREAM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Mcp-Session-Id", sessionId)
                        .content("""
                                {
                                    "jsonrpc":"2.0",
                                    "id":1,
                                    "method":"tools/list"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    JsonNode root = getJsonNode(result);
                    assertThat(root.at("/result/tools/0/name").asText()).isEqualTo("greeting");
                });
                //.andExpect(jsonPath("$.result.tools[0].name").value("greeting")); // not working because it's event stream method

        mockMvc.perform(post("/mcp")
                        .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_EVENT_STREAM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Mcp-Session-Id", sessionId)
                        .content("""
                                {
                                  "jsonrpc":"2.0",
                                  "id":2,
                                  "method":"tools/call",
                                  "params": {
                                    "name":"greeting",
                                    "arguments": {
                                      "arg0":"MCP",
                                      "arg1":"ko"
                                    }
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    JsonNode root = getJsonNode(result);
                    assertThat(root.at("/result/content/0/text").asText()).isEqualTo("안녕하세요, MCP!");
                });
                //.andExpect(jsonPath("$.result.content[0].text").value("안녕하세요, MCP!"));    // not working because it's event stream method
    }

    private static JsonNode getJsonNode(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8)
                .lines()
                .filter(line -> line.startsWith("data:"))
                .map(line -> line.substring(5).trim())
                .findFirst()
                .orElseThrow();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(json);
    }
}
