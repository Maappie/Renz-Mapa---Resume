---
paths:
  - "src/main/java/**"
---

# REST API Design

## Paths

- Every `@RestController` is auto-prefixed with `/api` by `config/WebConfig.java`.
  **Never write `/api` in a `@RequestMapping`.** `@RequestMapping("/projects")` → `/api/projects`.
- Resource paths are plural nouns in kebab-case: `/projects`, `/skills/grouped`, `/work-history`.
  No verbs in paths (`/getProjects`, `/createSkill` are wrong — the HTTP method is the verb).
- Identify a single resource with a path variable: `/projects/{id}`.

## HTTP methods

| Method   | Meaning                                  | Success status              |
|----------|------------------------------------------|-----------------------------|
| `GET`    | Read. Never changes state.               | `200 OK`                    |
| `POST`   | Create a new resource                    | `201 Created` + created body |
| `PATCH`  | Partial update — only sent fields change | `200 OK` + updated body     |
| `PUT`    | Full replacement — all fields provided   | `200 OK` + updated body     |
| `DELETE` | Remove                                   | `204 No Content`            |

## Status codes — always explicit

- Every handler returns `ResponseEntity<T>` with a deliberate status.
- Missing resource → `ResponseEntity.notFound().build()` (**404**). Never `200` with `null` or an
  empty body.
- Invalid input → **400** with a short message saying which field and why.
- Never return `500` deliberately; an unexpected exception is a bug to fix.

## PATCH semantics (non-negotiable)

- Request DTO fields are all nullable. The service applies only non-null fields:
  ```java
  if (request.getName() != null) existing.setName(request.getName());
  ```
- `null` = "keep current value"; `""` = "clear the field". State this in the endpoint's Javadoc.
- Never replace a whole entity when only some fields were sent.

## DTOs at every boundary

- **Never accept a JPA entity as `@RequestBody`.** Use `{Feature}Request`.
- **New endpoints return response DTOs** (`{Feature}Response`, a `record`), mapped in the service.
  Several existing endpoints still return entities directly (`ResponseEntity<Project>`); that is
  legacy. Do not add new ones. When you change an existing endpoint's response shape, introduce a
  response DTO for it as part of that change.
- Never expose sensitive or internal fields (passwords, internal flags, lazy relations) — this is
  the moment a response DTO becomes mandatory.
- DTO names: `{Feature}{Action}Request` or `{Feature}Request`, `{Feature}Response`.

## Service result contract

Existing slices use: lookup returns the object or `null`; delete returns `boolean`. The controller
maps that to `404`. Keep that contract within a slice and document it in the method's Javadoc
(`@return the Project, or null if not found`). Do not mix `Optional` and `null` returns in one slice.

## Compatibility

- Renaming or removing a JSON field, path, or changing a type is a **breaking change**. Do not make
  one without asking. If approved, version the API (`/api/v2/...`) instead of breaking `/api/...`.
- Adding a new optional field is non-breaking.
- When an API shape changes, update the matching `static/js/api/*.js` module and its consumers in the
  same change.

## OpenAPI / Swagger documentation

Every controller and endpoint is documented for Swagger UI, matching `ProjectController`:

- Class: `@Tag(name = "Projects")`.
- Method: `@Operation(summary = ..., description = ...)` and one `@ApiResponse` per possible status
  (200/201/204/400/404).
- Path variables: `@Parameter(description = ..., example = ...)`.
- Request/response DTO fields: `@Schema(description = ..., example = ..., nullable = true)`.
- A new feature must also be registered in `config/OpenApiConfig.java`: add its `Tag` to the
  ordered tag list and a `GroupedOpenApi` bean for its path, following the existing entries.
