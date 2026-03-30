# Spring OpenAPI + JSON Schema + MCP Server Example

This project is a minimal sample that combines:

- Spring Boot REST API
- OpenAPI docs exposure via `springdoc`
- JSON Schema validation via `json-schema-validator`
- MCP JSON-RPC endpoint at `/mcp`

## Requirements

- Java 17+
- Maven 3.9+

## Run

```powershell
mvn spring-boot:run
```

After the app starts:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- REST API: `POST http://localhost:8080/api/greet`
- MCP endpoint: `POST http://localhost:8080/mcp`

## Quick API examples

```powershell
curl -X POST "http://localhost:8080/api/greet" -H "Content-Type: application/json" -d '{"name":"Copilot","language":"en"}'
curl -X POST "http://localhost:8080/mcp" -H "Content-Type: application/json" -d '{"jsonrpc":"2.0","id":1,"method":"tools/list"}'
```

## MCP tools/call example

```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "method": "tools/call",
  "params": {
    "name": "greet",
    "arguments": {
      "name": "MCP",
      "language": "ko"
    }
  }
}
```

## Test

```powershell
mvn test
```
