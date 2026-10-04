# CLAUDE.md

Guidance for Claude Code when working in this repository.

## Project Overview

Renz Mapa's resume/portfolio system: a Spring Boot 4 REST API (`com.renzmapa.resume_api`, Java 21)
backed by SQLite, serving a vanilla HTML/CSS/JS frontend as static assets (no Thymeleaf/templating).
Spring Boot Admin + SpringDoc/Swagger are mounted under `/admin` for ops and API docs.

- Backend root package: `src/main/java/com/renzmapa/resume_api`
- Frontend: `src/main/resources/static`
- All `@RestController` classes are auto-prefixed with `/api` (see `config/WebConfig.java`) — never
  hardcode `/api` in a `@RequestMapping`.

## Commands

- Run locally: `./mvnw spring-boot:run` (Windows: `.\mvnw.cmd spring-boot:run`) → http://localhost:8080
- Compile: `./mvnw compile`
- Run tests: `./mvnw test`
- Build: `./mvnw clean package`
- Spring Boot Admin UI: http://localhost:8080/admin
- Swagger UI: http://localhost:8080/admin/api-docs

## Engineering Rules

The binding rules live in `.claude/rules/` and are the **single source of truth**. Claude Code loads
them automatically; the files under `coding/` and `web/` load when you work on matching files. Do not
restate rules here — to change one, edit its file.

| File | Covers | Applies to |
|------|--------|------------|
| `rules/workflow.md` | Scope discipline, protected files, **Definition of Done** checks, reporting | Always |
| `rules/git.md` | When to commit, Conventional Commits format, commit hygiene | Always |
| `rules/security.md` | Secrets, input validation, XSS, exposed surface | Always |
| `rules/documentation.md` | Which docs to update, Javadoc/JSDoc, comments | Always |
| `rules/coding/architecture.md` | Feature-first packages, layering, constructor DI | Java |
| `rules/coding/java-style.md` | Naming, imports, method size, records, Lombok policy | Java |
| `rules/coding/api-design.md` | REST paths/verbs, status codes, PATCH, DTOs, OpenAPI, versioning | Java |
| `rules/coding/persistence.md` | Entities, repositories, transactions, seeders, migrations, `resume.db` | Java, properties |
| `rules/coding/error-handling-logging.md` | Exceptions, SLF4J, log levels | Java |
| `rules/coding/testing.md` | What must be tested, test types, keeping tests off `resume.db` | Java, tests |
| `rules/web/javascript.md` | `api/` vs `components/` vs `main.js`, module and async style | `static/` JS/HTML |
| `rules/web/css.md` | Tokens in `global.css`, per-page sheets, BEM, breakpoints | `static/` CSS/HTML |
| `rules/web/html-accessibility.md` | Semantic markup, WCAG AA, pinned third-party scripts | `static/` HTML |

Non-negotiables, whatever you are touching:

1. **Feature-first packages** and strict `Controller → Service → Repository` layering.
2. **DTOs at API boundaries**, constructor injection only, explicit `ResponseEntity` status codes.
3. **Frontend:** `fetch()` only in `js/api/`, rendering only in `js/components/`, design tokens only
   in `global.css`, no inline styles.
4. **A change is done only when the Definition of Done in `rules/workflow.md` passes** — and you
   report what you ran.
5. **Never edit `bin/`, `target/`, or `resume.db`.** Never commit unless asked.

## Reference Documentation

Consult before scaffolding new work:

- `documentation/boilerplate.md` — the pattern for a new feature slice; mirror `project/`.
- `documentation/model.md` — data model reference.
- `documentation/profile.md`, `documentation/profile-settings-guide.md` — Profile feature behavior.
- `documentation/setup.md` — full local setup, URLs, API reference, troubleshooting.
