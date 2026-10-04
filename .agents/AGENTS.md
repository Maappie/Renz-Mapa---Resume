# Engineering Rules — Renz Mapa Resume/Portfolio System

The engineering rules for this repository live in **`.claude/rules/`**. That folder is the single
source of truth for every agent and developer, not only Claude Code. Read the files that apply to
your change before you make it; they are binding.

## Always read

- `.claude/rules/workflow.md` — scope discipline, protected files, Definition of Done
- `.claude/rules/git.md` — commit rules and Conventional Commits format
- `.claude/rules/security.md` — secrets, input validation, XSS
- `.claude/rules/documentation.md` — which docs to update, comment standards

## Backend (Java / Spring Boot) — `.claude/rules/coding/`

- `architecture.md` — feature-first packages, Controller → Service → Repository, constructor DI
- `java-style.md` — naming, imports, method size, records
- `api-design.md` — REST conventions, status codes, PATCH, DTOs, OpenAPI, versioning
- `persistence.md` — entities, repositories, transactions, seeders, migrations
- `error-handling-logging.md` — exceptions and SLF4J logging
- `testing.md` — what must be tested and how

## Frontend (`src/main/resources/static`) — `.claude/rules/web/`

- `javascript.md` — `api/` / `components/` / `main.js` responsibilities
- `css.md` — design tokens, per-page stylesheets, BEM, breakpoints
- `html-accessibility.md` — semantic markup, accessibility, third-party scripts

Do not add rules to this file. To change a rule, edit its file in `.claude/rules/`.
