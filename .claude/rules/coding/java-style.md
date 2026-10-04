---
paths:
  - "src/main/java/**"
  - "src/test/java/**"
---

# Java Code Style

Target: **Java 21**. Use modern language features where they make code clearer.

## Files and naming

- One public top-level type per file; file name matches the type name exactly.
- `PascalCase` types, `camelCase` methods and fields, `SCREAMING_SNAKE_CASE` constants,
  lowercase package names (`resume_api` is the existing exception — keep it).
- Names say what something *is* or *does*: `findAllByOrderBySortOrderAsc`, not `getData`.
- Booleans read as predicates: `featured`, `isFeatured()`, `hasDemo()`.

## Imports

- **No wildcard imports** (`import jakarta.persistence.*;` is forbidden). Import exactly what is used.
- No unused imports.

## Methods and classes

- Aim for **≤ 20 lines per method**. Longer → extract well-named private helpers
  (see `ProjectService.applyPatch`).
- One level of abstraction per method. A method either orchestrates or does detail work, not both.
- Prefer early returns over nested `if`/`else`.
- Max ~4 parameters on a new method you write; beyond that, pass a DTO/record. (JPA entity
  constructors used by seeders are the accepted exception.)
- No magic numbers or strings — name them as `private static final` constants.

## Types

- Use **records** for immutable data carriers: response DTOs, value objects, grouped results
  (see `SkillGroupResponse`). Entities and request DTOs stay classes (JPA and patch semantics need it).
- Use boxed types (`Boolean`, `Integer`) in request DTOs so "not sent" (`null`) is distinguishable
  from `false`/`0`. Primitives are fine in entities for non-nullable columns.
- Return empty collections, never `null` collections.
- Prefer `List.of(...)` / `Map.of(...)` for constants; copy defensively when storing a caller's
  collection (`new ArrayList<>(list)`), as `Project`'s constructor does.
- Use streams when they read more clearly than a loop; don't force them.
- `var` only when the type is obvious from the right-hand side.

## Lombok

Lombok is on the classpath but the codebase writes getters, setters, and constructors by hand.
**Do not introduce Lombok annotations** — keep the slices consistent. If you think Lombok should be
adopted, propose it to the user as a separate change.

## Hygiene

- No commented-out code. No `System.out.println` / `printStackTrace` (see `error-handling-logging.md`).
- No `@SuppressWarnings` without a comment explaining why.
- Match the surrounding formatting: 4-space indentation, aligned getter/setter blocks as in existing
  entities, section divider comments (`// ─── Getters ───`) where the file already uses them.
