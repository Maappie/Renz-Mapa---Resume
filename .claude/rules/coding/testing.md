---
paths:
  - "src/main/java/**"
  - "src/test/java/**"
  - "pom.xml"
---

# Testing

Stack: JUnit 5, Mockito, AssertJ and MockMvc — all provided by `spring-boot-starter-webmvc-test`.
Run with `./mvnw test` (Windows: `.\mvnw.cmd test`).

## What must be tested

- **Every new or changed service method with logic** gets unit tests: happy path, not-found path,
  and each branch (e.g. PATCH with null vs non-null fields, grouping/ordering rules).
- **Every new endpoint** gets a controller test asserting status codes (200/201/204/400/404) and the
  JSON shape the frontend relies on.
- **Every bug fix** starts with a test that reproduces the bug, then the fix makes it pass.
- Pure getters/setters, records, and Spring wiring do not need their own tests.

## How to write them

| Test kind        | Use                                         | Location                                  |
|------------------|---------------------------------------------|-------------------------------------------|
| Service unit     | Plain JUnit + `@ExtendWith(MockitoExtension.class)`, mock the repository | `src/test/java/.../{feature}/{Feature}ServiceTest.java` |
| Controller slice | `@WebMvcTest({Feature}Controller.class)` + `MockMvc`, mock the service with `@MockitoBean` | `.../{feature}/{Feature}ControllerTest.java` |
| Repository       | `@DataJpaTest` (only for custom queries)    | `.../{feature}/{Feature}RepositoryTest.java` |
| Full context     | `@SpringBootTest` — sparingly; it is slow   | `ResumeApiApplicationTests`               |

- Test packages mirror the main packages (feature-first).
- Name tests by behaviour: `patch_keepsExistingValue_whenFieldIsNull()`,
  `getById_returns404_whenMissing()`.
- Structure each test as **arrange / act / assert**, one behaviour per test.
- Use AssertJ (`assertThat(...)`) for assertions.
- Tests are independent: no shared mutable state, no ordering dependencies.
- `WebConfig` adds the `/api` prefix, and `@WebMvcTest` picks up `WebMvcConfigurer` beans, so
  controller tests call the real path: `/api/projects`, not `/projects`.

## Tests must never touch `resume.db`

`application.properties` points at `jdbc:sqlite:resume.db`, so a `@SpringBootTest` or
`@DataJpaTest` with default config reads and writes the real development database. Any test that
loads a datasource must override it — e.g. `src/test/resources/application.properties` with an
in-memory or temp-file SQLite URL — before it is added. If that override does not exist yet and you
need one, create it as part of your change.

## Never

- Never delete, `@Disabled`, or weaken an assertion to make a failing test pass. Fix the code, or
  stop and report why the test is wrong.
- Never mark work done with failing tests.
