---
paths:
  - "src/main/java/**"
  - "src/test/java/**"
---

# Architecture — Packages, Layering, Dependency Injection

## 1. Feature-first packages

Organise by **feature**, not by layer. Each feature owns its full vertical slice.

```
com.renzmapa.resume_api
├── profile/  project/  skill/  experience/  education/  feature/   ← feature slices
├── admin/    ← monitoring/observability config only — never business features
├── config/   ← cross-cutting infrastructure beans (MVC, OpenAPI, CORS) — no business logic
└── ResumeApiApplication.java
```

- **Never** create top-level `controllers/`, `services/`, `repositories/`, `dto/`, `model/`, or
  `utils/` packages.
- A new feature gets its own sub-package as soon as it has more than one class.
- A slice contains, by convention (mirror `project/`):

  | Class                    | Role                                              |
  |--------------------------|---------------------------------------------------|
  | `{Feature}`              | JPA entity                                        |
  | `{Feature}Repository`    | Spring Data interface                             |
  | `{Feature}Service`       | Business logic                                    |
  | `{Feature}Controller`    | HTTP mapping                                      |
  | `{Feature}Request`       | Request DTO (create + patch)                      |
  | `{Feature}Response` / `{Feature}…Response` | Response DTO (record) when needed |
  | `{Feature}Seeder`        | Idempotent first-boot data (`CommandLineRunner`)  |

- Slices do not reach into each other's repositories. If `project` needs skill data, it calls
  `SkillService`, never `SkillRepository`.

## 2. Strict layering

```
Controller  →  Service  →  Repository
```

| Layer          | Allowed                                                   | Forbidden                                          |
|----------------|-----------------------------------------------------------|----------------------------------------------------|
| Controller     | Parse input, call **one** service method, map result to `ResponseEntity` | `if/else` business rules, repository calls, loops over data |
| Service        | All business logic, validation, mapping entity ↔ DTO      | `ResponseEntity`, `HttpStatus`, `HttpServletRequest`, any web type |
| Repository     | Data access (derived queries, `@Query`)                   | Logic, formatting, default values                  |
| Entity         | Persistence state + getters/setters                       | Business logic, formatting, JSON/HTTP concerns     |

The only conditional allowed in a controller is mapping a service result to a status code
(`result == null ? notFound() : ok(result)`).

## 3. Dependency injection — constructor only

```java
private final ProjectRepository projectRepository;

public ProjectService(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
}
```

- Never `@Autowired` on fields or setters. A single constructor needs no annotation.
- Every injected field is `private final`.
- Depend on the narrowest thing you need — inject a service, not the `ApplicationContext`.

## 4. Before adding any feature, answer

1. Does it belong in its own package? (More than one class → yes.)
2. Is the service doing the work and the controller thin?
3. Do DTOs separate the API contract from the database schema?
4. Are all fields in the update request nullable, so PATCH works from day one?
5. Does it change an existing endpoint's contract? If yes → version it (see `api-design.md`).
