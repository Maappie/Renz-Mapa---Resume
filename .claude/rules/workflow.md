# Workflow — How Every Change Is Made

These rules apply to every task, backend or frontend.

## Before writing code

- **Read before you write.** Open the files you will change and at least one sibling that already
  does the same job (e.g. `project/` when adding a slice, `projectApi.js` when adding an API module).
  Match what is there — naming, comment density, Javadoc style, layout.
- **New feature slice → follow `documentation/boilerplate.md`** and mirror `project/` (entity,
  repository, service, controller, request DTO, seeder). Do not invent a new layout.
- **Unclear requirement → ask.** If a request has two reasonable readings that lead to different
  code, ask one focused question instead of guessing.

## Scope discipline

- Change only what the task needs. No drive-by refactors, renames, reformatting, or "while I was
  here" fixes inside the same change.
- If you spot an unrelated problem (a rule violation, a bug), **report it** at the end of your
  reply — do not silently fix it.
- Never add a dependency to `pom.xml` or a new CDN script without asking first. State what it is
  for and what the alternative without it would be.
- Never delete or rename public endpoints, DTO fields, or JSON keys without asking — the frontend
  and any API consumer depend on them (see `coding/api-design.md` on versioning).

## Files you must not touch

- `bin/` — local IDE/build output mirroring `src/`. Never edit; it is not source of truth.
- `target/` — Maven build output.
- `resume.db` — never hand-edit or open with a write-capable tool. Data changes go through the API,
  a seeder, or a migration (see `coding/persistence.md`).
- `mvnw`, `mvnw.cmd`, `.mvn/` — Maven wrapper; only change via `mvnw wrapper:wrapper`.

## Definition of done

A change is **not done** until every applicable check below has been run and passed. Report the
result of each check in your reply — if one was skipped or failed, say so plainly.

**Backend change** (anything under `src/main/java`, `src/test/java`, `pom.xml`, `application.properties`):

1. `./mvnw compile` — compiles cleanly (Windows: `.\mvnw.cmd compile`).
2. `./mvnw test` — all tests pass.
3. New or changed service logic has tests (see `coding/testing.md`).
4. New endpoints are visible and correct in Swagger UI (`/admin/api-docs`) when the app is running.

**Frontend change** (anything under `src/main/resources/static`):

1. No `fetch(` outside `static/js/api/` (exception: `api-explorer.js`, whose purpose is to send
   arbitrary requests):
   `grep -rn "fetch(" src/main/resources/static --include=*.js --include=*.html | grep -v "/js/api/" | grep -v api-explorer.js`
2. No hex colours outside `global.css`:
   `grep -nE "#[0-9a-fA-F]{3,8}\b" src/main/resources/static/css/*.css | grep -v global.css`
3. No inline styles: `grep -rn 'style="' src/main/resources/static --include=*.html`
4. No business logic or `fetch` in `js/components/`; no DOM construction in `main.js`.
5. Open the page in a browser at desktop, 900px and 640px widths and check the browser console is
   clean.

Pre-existing violations found by these checks are not yours to fix unless asked — but list them.

## Reporting

- End with: what changed (files), what was verified and how, and anything left undone or found
  along the way.
- Never claim a check passed that you did not run.
