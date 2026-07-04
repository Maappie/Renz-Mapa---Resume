# Engineering Rules — Renz Mapa Resume/Portfolio System

These rules apply to every change made to this codebase, by any agent or developer.
They are written from the perspective of a senior engineer building a system that starts
small but is designed to grow cleanly without rewrites.

---

## 1. Package Structure — Feature-First, Always

Organise code by **feature**, not by layer. Every feature owns its full vertical slice.

```
com.renzmapa.resume_api
├── profile/          ← Feature slice (entity, repo, service, controller, DTOs)
├── admin/            ← Admin / monitoring config
├── config/           ← Cross-cutting Spring config (CORS, security, MVC)
└── ResumeApiApplication.java
```

**Rules:**
- Never create top-level `controllers/`, `services/`, or `repositories/` packages.
- Each new feature (e.g. `projects`, `skills`, `messages`) gets its own sub-package.
- `config/` is only for infrastructure config beans — no business logic lives there.
- `admin/` is only for monitoring/observability config — never for business features.

---

## 2. Layering Contract — Strict Call Direction

```
Controller  →  Service  →  Repository
```

- **Controllers** handle HTTP only: parse input, call one service method, return `ResponseEntity`.
- **Services** contain all business logic. They are the only callers of repositories.
- **Repositories** do data access only. No logic, no formatting.
- **Entities** are persistence models only. No business logic, no formatting, no API concerns.

**Never:**
- Call a repository directly from a controller.
- Put `if/else` business logic inside a controller method.
- Put HTTP-specific types (`HttpServletRequest`, `ResponseEntity`) inside a service.

---

## 3. DTOs at Every API Boundary

**Never expose JPA entities directly in API responses or accept them as request bodies.**

| Direction       | Type to use              | Example                    |
|-----------------|--------------------------|----------------------------|
| API → Service   | Request DTO (class/record) | `ProfileUpdateRequest`    |
| Service → API   | Response DTO or entity   | `Profile` (only if safe)  |

- **Request DTOs** (e.g. `ProfileUpdateRequest`) must have all fields nullable to support partial updates.
- When an entity grows complex or has sensitive fields, introduce a **response DTO** to decouple the API shape from the database schema.
- DTO class names follow the pattern: `{Feature}{Action}Request` / `{Feature}Response`.

---

## 4. Partial Updates — PATCH, Not PUT

Use `PATCH` for any update that does not require all fields.

**Service-layer patch pattern (non-negotiable):**
```java
if (request.getField() != null) existing.setField(request.getField());
```

- Never replace entire entities when only a subset of fields was sent.
- `PUT` is reserved for full replacements where all fields are intentionally provided.
- Empty string `""` and `null` are treated differently: `null` = "keep current", `""` = "clear the field". Document this in the endpoint's Javadoc.

---

## 5. Dependency Injection — Constructor Only

Always use constructor injection. Never use `@Autowired` on fields.

```java
// Correct
public ProfileService(ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
}

// Never do this
@Autowired
private ProfileRepository profileRepository;
```

- All injected fields must be `private final`.
- This makes the dependency graph visible, testable, and mockable.

---

## 6. Java Code Style

- **One public class per file.** File name must match the class name exactly.
- **No wildcard imports.** `import java.util.*` is forbidden. Import only what is used.
- **camelCase** for fields and methods, **PascalCase** for classes, **SCREAMING_SNAKE_CASE** for constants.
- Method length: aim for **20 lines max**. If a method is longer, break it into private helpers.
- No commented-out dead code. Remove it, or use version control to retrieve it.
- Every public method on a service or controller must have a **Javadoc comment** describing what it does, its parameters, and return value.

---

## 7. API Design Conventions

- **Base path for REST:** All `@RestController` classes are automatically prefixed with `/api` via `WebConfig`. Do not manually add `/api` to `@RequestMapping`.
- **HTTP verb semantics:**
  - `GET` — read, never modifies state
  - `POST` — create a new resource
  - `PATCH` — partial update of an existing resource
  - `PUT` — full replacement of an existing resource
  - `DELETE` — remove a resource
- **Status codes must be explicit** via `ResponseEntity`. Never return `200 OK` for a not-found result.
- **404 on missing resource:** Return `ResponseEntity.notFound().build()` when `id` is not found — never return `null` or an empty body.

---

## 8. Database and Entity Rules

- JPA entities live in their feature package. They are annotated with `@Entity` and `@Table`.
- **`ddl-auto=update`** is acceptable in development. Before going live, change to `validate` and manage schema with a migration tool (Flyway or Liquibase).
- All entity fields start `private`. Expose them only via getters.
- Setters are only added when a field must be mutable (e.g. for patch operations). Do not add setters by default.
- Collection fields (e.g. `List<String>`) must be initialised (`= new ArrayList<>()`) to prevent NPEs.
- Never use `@GeneratedValue(strategy = GenerationType.AUTO)` — always be explicit: `IDENTITY` for SQLite, `SEQUENCE` for PostgreSQL.

---

## 9. Frontend JavaScript Conventions

Organise JS by responsibility:

```
js/
├── api/           ← All fetch() calls live here. No fetch() elsewhere.
├── components/    ← DOM rendering functions. No business logic.
└── main.js        ← Entry point. Wires api + components. No inline fetch().
```

- **No inline `fetch()` calls** in `main.js` or HTML `<script>` tags.
- All API modules export async functions that return parsed data or throw errors.
- Component functions accept data as arguments and return/mutate DOM — they do not fetch.
- Use `async/await` over `.then()` chains for readability.
- Always wrap top-level `await` calls in `try/catch` with meaningful error handling.

---

## 10. CSS Architecture

- All design tokens (colors, fonts, spacing, shadows) are defined as CSS custom properties in `global.css` under `:root`.
- Do not hardcode color hex values or pixel sizes outside of `global.css`.
- Each page gets its own scoped stylesheet: `profile.css`, `settings.css`, etc.
- **No inline `style=""` attributes** in HTML. All styling goes through CSS classes.
- Responsive breakpoints are defined consistently at `640px` (mobile) and `900px` (tablet).

---

## 11. Scalability Checkpoints

Before adding any new feature, answer these:

1. **Does it belong in its own package?** If the feature has more than one class, give it a package.
2. **Is the service layer doing the work?** Controllers should be thin.
3. **Are DTOs separating the API contract from the database schema?** If not, add them.
4. **Are all fields nullable in update requests?** Support partial updates from day one.
5. **Does adding this feature break any existing endpoint?** If yes, version the API (`/api/v2/...`) instead of changing `/api/v1/...`.

---

## 12. Anti-patterns — Never Do These

| Anti-pattern | Why it is banned |
|---|---|
| Business logic in `@Controller` | Controllers should only speak HTTP |
| `@Autowired` field injection | Breaks testability and hides dependencies |
| Exposing JPA entities in responses | Leaks schema, breaks API/DB decoupling |
| Wildcard imports | Hides what is actually being used |
| Magic strings or numbers in code | Use named constants |
| `System.out.println` for logging | Use SLF4J (`private static final Logger log = ...`) |
| Catch-all `catch (Exception e) {}` | Always handle or rethrow with context |
| TODO comments older than one sprint | Either fix it or open a tracked issue |
