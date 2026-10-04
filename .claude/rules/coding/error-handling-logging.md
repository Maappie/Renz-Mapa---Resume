---
paths:
  - "src/main/java/**"
---

# Error Handling and Logging

## Exceptions

- **Never swallow exceptions.** `catch (Exception e) {}` and catch-and-ignore are forbidden.
  Either handle the error meaningfully or rethrow with context:
  ```java
  throw new IllegalStateException("Failed to seed projects", e);
  ```
- Catch the **narrowest** exception type you can actually handle. Do not catch `Exception` or
  `Throwable` except at a deliberate top-level boundary.
- Do not use exceptions for normal control flow (e.g. "not found" is a `null`/`false` result per
  the service contract in `api-design.md`, not an exception).
- Invalid client input is a **400**, not a 500. If several endpoints need the same error mapping,
  centralise it in one `@RestControllerAdvice` in `config/` rather than repeating try/catch in
  controllers — and return a consistent error body (`{ "error": "...", "field": "..." }`).
- Never leak stack traces or internal exception messages to API clients.

## Logging

- Use **SLF4J** only:
  ```java
  private static final Logger log = LoggerFactory.getLogger(ProjectSeeder.class);
  ```
- **Never** `System.out.println`, `System.err`, or `e.printStackTrace()`.
- Use parameterised messages, not string concatenation: `log.info("Seeded {} projects.", count);`
- When logging an exception, pass it as the last argument so the stack trace is kept:
  `log.error("Failed to load profile {}", id, e);`
- Levels:
  | Level   | For                                                              |
  |---------|------------------------------------------------------------------|
  | `error` | Something failed and needs attention                             |
  | `warn`  | Unexpected but recovered (fallback used, bad data skipped)       |
  | `info`  | Significant lifecycle events (startup, seeding, config choices)  |
  | `debug` | Detail useful while developing; off by default                   |
- Never log secrets or personal data (see `security.md`). Do not log inside tight loops.
