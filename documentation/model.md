# Model Guide

This document is the definitive reference for defining data models in this project.
All models must follow these conventions to ensure consistency, type safety, and clean JSON serialization.

---

## Table of Contents

1. [What is a Model?](#1-what-is-a-model)
2. [Why Java Records?](#2-why-java-records)
3. [Anatomy of a Model](#3-anatomy-of-a-model)
4. [Field Naming Conventions](#4-field-naming-conventions)
5. [Supported Field Types](#5-supported-field-types)
6. [JSON Serialization Mapping](#6-json-serialization-mapping)
7. [What a Model Must NOT Do](#7-what-a-model-must-not-do)
8. [Real Example: Profile](#8-real-example-profile)
9. [Boilerplate Template](#9-boilerplate-template)

---

## 1. What is a Model?

A **model** represents the shape of a single unit of data in your application.
It is the contract between the backend and the frontend — the server produces it, the browser consumes it.

In this project, models are used to:
- Define the structure of a REST API response.
- Guarantee type safety inside the Java service layer.
- Drive automatic JSON serialization via Spring Boot + Jackson.

Models live in the feature's own package under `src/main/java/`:

```text
src/main/java/com/renzmapa/resume_api/
└── {feature}/
    └── {Feature}.java      ← the model
```

---

## 2. Why Java Records?

Java `record`s (introduced in Java 16, stabilized in Java 17) are the **preferred** way to define models in this project over traditional classes.

| Feature | `record` | `class` |
|---|---|---|
| Immutable by default | ✅ | ✗ (requires `final` fields) |
| Auto-generated constructor | ✅ | ✗ (must write manually) |
| Auto-generated getters | ✅ (`field()` style) | ✗ (must write or use Lombok) |
| Auto-generated `equals` / `hashCode` | ✅ | ✗ (must override) |
| Auto-generated `toString` | ✅ | ✗ (must override) |
| Works with Jackson (JSON) | ✅ | ✅ |
| Boilerplate code required | None | Significant |

> [!TIP]
> Use a `class` only when you need mutability (e.g., a model that is updated after construction). In most REST API scenarios, you will never need that.

---

## 3. Anatomy of a Model

```java
package com.renzmapa.resume_api.profile;   // (1) Package declaration

import java.util.List;                     // (2) Imports (only what is used)

public record Profile(                     // (3) record keyword
    String name,                           // (4) Fields — one per line
    String title,
    String email,
    String phone,
    String location,
    String bio,
    List<String> socialLinks              // (5) Collections use List<T>
) {}                                       // (6) Empty body — no logic here
```

### Breaking it down

| Part | Rule |
|---|---|
| `package` | Must match the feature's directory path exactly. |
| `import` | Only import what you use. Never use wildcard imports (`import java.util.*`). |
| `record` keyword | Replaces `class`. All fields are implicitly `private final`. |
| Fields | `camelCase`, one per line, comma-separated. |
| `{}` body | Always empty. Logic does not belong in models. |

---

## 4. Field Naming Conventions

All field names must be `camelCase`. This is critical because Jackson (Spring's JSON library) maps record field names directly to JSON keys without any transformation.

| Java Field | JSON Key |
|---|---|
| `String name` | `"name"` |
| `String firstName` | `"firstName"` |
| `List<String> socialLinks` | `"socialLinks"` |
| `String githubUrl` | `"githubUrl"` |

> [!IMPORTANT]
> Never use `snake_case` or `PascalCase` for field names. The JSON contract your frontend depends on will break.

---

## 5. Supported Field Types

| Use case | Java type | JSON output |
|---|---|---|
| Plain text | `String` | `"value"` |
| Whole numbers | `Long` or `Integer` | `123` |
| Decimal numbers | `Double` | `1.5` |
| True/false | `Boolean` | `true` / `false` |
| List of strings | `List<String>` | `["a", "b"]` |
| List of objects | `List<FeatureItem>` | `[{...}, {...}]` |
| Nullable field | `String` (or wrap in `Optional`) | `null` |

> [!NOTE]
> Prefer `Long` over `int` / `long` for IDs. Prefer `List<T>` over arrays (`T[]`) for collections.

### Nesting models

A model can contain another model as a field. The nested model follows the exact same rules.

```java
public record Address(
    String street,
    String city,
    String country
) {}

public record Contact(
    String name,
    String email,
    Address address    // ← nested model
) {}
```

This produces the following JSON:
```json
{
  "name": "Renz Mapa",
  "email": "renz@example.com",
  "address": {
    "street": "123 Sample St",
    "city": "Manila",
    "country": "Philippines"
  }
}
```

---

## 6. JSON Serialization Mapping

Spring Boot uses **Jackson** to automatically convert Java records into JSON. No extra configuration is required.

The full flow when an API is called:

```
Client GET /api/profile
        │
        ▼
ProfileController.getProfile()
        │  returns a Profile record instance
        ▼
Jackson ObjectMapper
        │  reads each record component via accessor methods
        ▼
JSON Response body:
{
  "name": "Renz Mapa",
  "title": "Software Engineer",
  "email": "renz@example.com",
  "phone": "+63 900 000 0000",
  "location": "Manila, Philippines",
  "bio": "...",
  "socialLinks": ["https://github.com/Maappie", "https://linkedin.com/in/renzmapa"]
}
```

Jackson reads the record's **component accessor methods** (e.g., `name()`, `title()`) rather than traditional `getName()` getters. This is handled automatically — no annotations are needed for standard field-to-key mappings.

---

## 7. What a Model Must NOT Do

A model is a **passive data container**. It must never:

| What | Why |
|---|---|
| Call `fetch` or any I/O | Models have no knowledge of the network or database |
| Contain `if` statements or loops | Logic belongs in the service layer |
| Reference other Spring beans (`@Autowired`) | Models are not Spring components |
| Have setter methods | Immutability is intentional — data flows in one direction |
| Grow beyond ~15 fields | A bloated model is a sign the feature needs to be split |

> [!WARNING]
> If you catch yourself adding a method with logic to a record, that logic belongs in `{Feature}Service.java` instead.

---

## 8. Real Example: Profile

**File**: [Profile.java](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/java/com/renzmapa/resume_api/profile/Profile.java)

```java
package com.renzmapa.resume_api.profile;

import java.util.List;

public record Profile(
    String name,
    String title,
    String email,
    String phone,
    String location,
    String bio,
    List<String> socialLinks
) {}
```

**Produced JSON** (from [ProfileController.java](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/java/com/renzmapa/resume_api/profile/ProfileController.java)):

```json
{
  "name": "Renz Mapa",
  "title": "Software Engineer / Full-Stack Developer",
  "email": "renz.mapa@example.com",
  "phone": "+63 900 000 0000",
  "location": "Manila, Philippines",
  "bio": "A passionate developer practicing modern Java development with Spring Boot.",
  "socialLinks": [
    "https://github.com/Maappie",
    "https://linkedin.com/in/renzmapa"
  ]
}
```

**Consumed on the client** (from [profileApi.js](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/resources/static/js/api/profileApi.js)):

```js
const profile = await fetchProfileData();

document.getElementById('name').textContent = profile.name;
document.getElementById('email').textContent = profile.email;
```

---

## 9. Boilerplate Template

Copy this template when adding a new model. Replace every `{Feature}` / `{field}` placeholder.

```java
package com.renzmapa.resume_api.{feature};

import java.util.List;

/**
 * Immutable data model for the {Feature} resource.
 *
 * <p>Field names must be camelCase — they map 1:1 to JSON keys.
 *
 * @param id          Unique identifier.
 * @param fieldOne    Short description of this field.
 * @param fieldTwo    Short description of this field.
 * @param tags        Optional classification tags.
 */
public record {Feature}(
    Long id,
    String fieldOne,
    String fieldTwo,
    List<String> tags
) {}
```

**Checklist before committing a new model:**

- [ ] Package matches the feature directory (`com.renzmapa.resume_api.{feature}`)
- [ ] All field names are `camelCase`
- [ ] No logic inside the record body
- [ ] All imports are explicit (no wildcards)
- [ ] JSDoc comment describes each field's purpose
- [ ] Verified the JSON output matches what the frontend `js/api/` module expects
