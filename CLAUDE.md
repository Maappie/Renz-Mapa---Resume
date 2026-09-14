## CLAUDE.md

Guidance for Claude Code when working in this repository. Read this fully before making changes.

## Project Overview

Renz Mapa's resume/portfolio system: a Spring Boot 4 REST API (`com.renzmapa.resume_api`) backed by
SQLite, serving a vanilla HTML/CSS/JS frontend as static assets (no Thymeleaf/templating — `templates/`
is unused). Spring Boot Admin + SpringDoc/Swagger are mounted under `/admin` for ops and API docs.

- Backend root package: `src/main/java/com/renzmapa/resume_api`
- Frontend: `src/main/resources/static`
- All `@RestController` classes are auto-prefixed with `/api` (see `config/WebConfig.java`) — never
  hardcode `/api` in a `@RequestMapping`.

## Commands

- Run locally: `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`) → http://localhost:8080
- Run tests: `./mvnw test`
- Build: `./mvnw clean package`
- Spring Boot Admin UI: http://localhost:8080/admin
- Swagger UI: http://localhost:8080/admin/api-docs

## Engineering Standards — Required Reading

@.agents/AGENTS.md

The file above is this project's engineering rulebook and is binding for every change — Java package
structure, layering, DTOs, DI, API conventions, entity rules, and the frontend JS/CSS organization.
Do not restate or contradict it here; if a rule needs to change, edit `.agents/AGENTS.md` itself so
there is one source of truth (other tools/agents also read that file).

The pillars most relevant day-to-day:

1. **Feature-first packages.** Every feature is a vertical slice (`profile/`, `feature/`, ...) holding
   its own entity, repository, service, controller, and DTOs. Never create top-level `controllers/`,
   `services/`, or `repositories/` packages.
2. **Strict layering:** `Controller → Service → Repository`. Controllers are HTTP-only and thin;
   business logic lives in services; repositories are data-access only.
3. **DTOs at API boundaries** — never expose JPA entities directly as request/response bodies.
4. **Constructor injection only** — no `@Autowired` field injection.
5. **PATCH for partial updates**, `PUT` only for full replacement; explicit `ResponseEntity` status
   codes (404 on missing resource, never a silent `null`/200).
6. **Frontend JS by responsibility**, under `static/js/`:
   - `api/` — all `fetch()` calls, nothing else
   - `components/` — DOM rendering, no business logic, no fetching
   - `main.js` — entry point that wires `api/` + `components/`; no inline fetch anywhere
7. **CSS architecture**: design tokens (colors, spacing, fonts) live as custom properties in
   `global.css`; every page gets its own scoped stylesheet (`profile.css`, `settings.css`, ...); no
   inline `style=""`, no hardcoded colors/sizes outside `global.css`.

See `.agents/AGENTS.md` for the full rules, the anti-patterns table, and rationale.

## Reference Documentation

Consult before scaffolding new work:

- `documentation/boilerplate.md` — the established pattern for scaffolding a new feature slice; follow
  it when adding a feature so structure stays consistent with `profile/` and `feature/`.
- `documentation/model.md` — data model reference.
- `documentation/profile.md`, `documentation/profile-settings-guide.md` — Profile feature behavior.
- `documentation/setup-guide.md` — local environment setup.

## Workflow Expectations

- Before adding a feature, check `documentation/boilerplate.md` and mirror the existing `profile/`
  slice's shape (entity, repository, service, controller, request DTOs) rather than inventing a new
  layout.
- After a backend change, run `./mvnw test` (and `./mvnw compile` for a quick sanity check on larger
  edits) before considering the change done.
- After a frontend change touching `static/js` or `static/css`, verify no `fetch()` was added outside
  `js/api/`, no business logic leaked into `js/components/`, and no hardcoded color/size values leaked
  outside `global.css`.
- Do not edit files under `bin/` — it is a local IDE/build output mirror of `src/`, not source of truth.
- `resume.db` is the local SQLite data file; don't hand-edit it — go through JPA/migrations.
