---
paths:
  - "src/main/java/**"
  - "src/main/resources/application*.properties"
  - "src/main/resources/db/**"
---

# Persistence — Entities, Repositories, Data

Database: **SQLite** via `sqlite-jdbc` + `hibernate-community-dialects` (`SQLiteDialect`).

## Entities

- Live in their feature package; annotated `@Entity` and `@Table(name = "<plural_snake_case>")`.
- IDs: `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)` with `Long`. **Never
  `GenerationType.AUTO`** — be explicit (`IDENTITY` for SQLite, `SEQUENCE` if moved to PostgreSQL).
- All fields `private`. Getters always; **setters only for fields the patch service mutates**.
- A `protected` no-arg constructor for JPA (`// Required by JPA`) plus a full constructor for
  creation.
- Collections are initialised (`= new ArrayList<>()`) and defensively copied in constructors.
- `@ElementCollection` tables are named `{entity}_{field}` with an explicit `@JoinColumn`
  (see `project_highlights`).
- Long text gets an explicit `@Column(length = ...)`.
- Enum-like strings (`accent`, `icon`) are documented on the field with their allowed values.
- Display order is data: use an `Integer sortOrder` column, not code-side ordering.
- No business logic, formatting, or JSON annotations (`@JsonIgnore`, etc.) in entities — if you need
  those, you need a response DTO.

## Repositories

- `interface {Feature}Repository extends JpaRepository<{Feature}, Long>`.
- Prefer derived query methods (`findAllByOrderBySortOrderAsc`). Use `@Query` with named/bound
  parameters only when a derived name becomes unreadable. Never concatenate user input into a query.
- Only services call repositories.

## Transactions

- A service method that performs **more than one write**, or a read-then-write that must be
  consistent, is annotated `@Transactional` (from `org.springframework.transaction.annotation`).
- Read-only multi-query methods may use `@Transactional(readOnly = true)`.
- Never put `@Transactional` on controllers.

## Seed data

- Each slice may have a `{Feature}Seeder implements CommandLineRunner`.
- Seeders **must be idempotent**: do nothing if the table already has rows (`repository.count() > 0`),
  and log what they did via SLF4J. They must never overwrite data edited through the API.

## Schema and the database file

- `spring.jpa.hibernate.ddl-auto=update` is acceptable **for local development only**.
- Before deployment, switch to `validate` and manage schema with **Flyway** migrations in
  `src/main/resources/db/migration/` (`V1__init.sql`, `V2__...sql`). Adding Flyway is a dependency
  change — ask first (see `workflow.md`).
- Once migrations exist, never edit an applied migration; add a new one.
- `resume.db` is the local data file. Never hand-edit it, delete it, or run destructive SQL against it
  without explicit permission — it currently holds the only copy of the resume content.
- Removing or renaming an entity field drops/renames a column: treat it as a breaking change and
  ask first.
