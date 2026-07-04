# Boilerplate Guide

This document defines the **standardized patterns** for adding new features to this project.
All new server-side and client-side code should follow these conventions without exception.

---

## Table of Contents

1. [Architecture Overview](#1-architecture-overview)
2. [Server-Side Boilerplate (Spring Boot)](#2-server-side-boilerplate-spring-boot)
   - [Folder Convention](#folder-convention)
   - [The Model (Record)](#the-model-record)
   - [The Service](#the-service)
   - [The Controller](#the-controller)
3. [Client-Side Boilerplate (Vanilla JS)](#3-client-side-boilerplate-vanilla-js)
   - [Folder Convention](#folder-convention-1)
   - [The API Module](#the-api-module)
   - [The Component Module](#the-component-module)
4. [Layer Responsibility Rules](#4-layer-responsibility-rules)
5. [Full Project Structure Reference](#5-full-project-structure-reference)

---

## 1. Architecture Overview

This project follows a strict **layered architecture**. Each layer has a single, clearly defined job:

```
                                          Browser
                                             │
                                    ┌────────▼────────┐
                                    │    page.html     │   Markup only
                                    └────────┬────────┘
                                             │ imports
                                    ┌────────▼────────┐
                                    │    js/main.js    │   Orchestration
                                    └───┬──────────┬──┘
                                        │          │
                           ┌────────────▼─┐    ┌───▼────────────┐
                           │  js/api/*.js  │    │ js/components/ │
                           │  Data fetch   │    │  DOM rendering │
                           └──────────────┘    └────────────────┘
                                        │
                          HTTP (REST /api/...)
                                        │
                                ┌───────▼───────┐
                                │  *Controller  │   HTTP routing
                                └───────┬───────┘
                                        │
                                ┌───────▼───────┐
                                │   *Service    │   Business logic
                                └───────┬───────┘
                                        │
                                ┌───────▼───────┐
                                │   *Record     │   Data model
                                └───────────────┘
```

---

## 2. Server-Side Boilerplate (Spring Boot)

### Folder Convention

Every new feature lives in its own **package**, named after the feature. This is called **package-by-feature** and ensures all related files are co-located.

```
src/main/java/com/renzmapa/resume_api/
└── {feature}/              ← one package per feature
    ├── {Feature}.java          Model
    ├── {Feature}Service.java   Business logic
    └── {Feature}Controller.java HTTP interface
```

**Example**: Adding a "Skills" feature creates a `skills/` package with `Skill.java`, `SkillService.java`, and `SkillController.java`.

---

### The Model (Record)

Reference: [Feature.java](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/java/com/renzmapa/resume_api/feature/Feature.java)

Use a Java `record` for all domain models. Records are immutable by design, which prevents accidental state mutation.

```java
package com.renzmapa.resume_api.{feature};

import java.util.List;

public record {Feature}(
    Long id,
    String fieldOne,
    String fieldTwo,
    List<String> tags
) {}
```

**Rules:**
- Field names must be `camelCase` — these map directly to JSON keys.
- Use `record` over `class` unless you specifically need mutability (you usually don't).
- Do not add business logic to models. They are data containers only.

---

### The Service

Reference: [FeatureService.java](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/java/com/renzmapa/resume_api/feature/FeatureService.java)

All business logic lives here. The controller delegates to this class; it never computes anything itself.

```java
package com.renzmapa.resume_api.{feature};

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class {Feature}Service {

    public List<{Feature}> getAll() {
        // TODO: Replace with repository call when database is added
        return List.of(...);
    }

    public {Feature} getById(Long id) {
        return getAll().stream()
            .filter(item -> item.id().equals(id))
            .findFirst()
            .orElse(null);
    }
}
```

**Rules:**
- Annotate with `@Service` — Spring manages its lifecycle.
- Return `null` for not-found cases; the controller handles the HTTP 404 translation.
- This is the **only** layer that changes when a database is introduced.

---

### The Controller

Reference: [FeatureController.java](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/java/com/renzmapa/resume_api/feature/FeatureController.java)

The controller is deliberately thin. It handles HTTP, nothing else.

```java
package com.renzmapa.resume_api.{feature};

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/{features}")
public class {Feature}Controller {

    private final {Feature}Service {feature}Service;

    public {Feature}Controller({Feature}Service {feature}Service) {
        this.{feature}Service = {feature}Service;
    }

    @GetMapping
    public ResponseEntity<List<{Feature}>> getAll() {
        return ResponseEntity.ok({feature}Service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<{Feature}> getById(@PathVariable Long id) {
        {Feature} item = {feature}Service.getById(id);
        return item == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(item);
    }
}
```

**Rules:**
- Use **constructor injection** — never `@Autowired` on a field.
- Never add `@RequestMapping("/api/...")` — the `/api` prefix is automatically applied by `WebConfig` to all `@RestController` classes.
- Always return `ResponseEntity<T>` to allow explicit HTTP status control.
- Keep methods under 10 lines. If a method grows, the logic belongs in the service.

---

## 3. Client-Side Boilerplate (Vanilla JS)

### Folder Convention

```
src/main/resources/static/js/
├── api/                     ← Network / fetch modules (no DOM)
│   ├── profileApi.js
│   └── {feature}Api.js
├── components/              ← UI rendering modules (no fetch)
│   ├── socialButtons.js
│   └── {feature}Card.js
└── main.js                  ← Page entry point and orchestrator
```

---

### The API Module

Reference: [featureApi.js](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/resources/static/js/api/featureApi.js)

One file per resource. Handles all `fetch` calls to the Spring Boot backend.

```js
// js/api/{feature}Api.js

const BASE_URL = '/api/{features}';

export async function fetchAll{Features}() {
    const response = await fetch(BASE_URL);

    if (!response.ok) {
        throw new Error(`Failed to fetch {features}: ${response.status}`);
    }

    return response.json();
}

export async function fetch{Feature}ById(id) {
    const response = await fetch(`${BASE_URL}/${id}`);

    if (response.status === 404) {
        throw new Error(`{Feature} with id "${id}" was not found.`);
    }

    if (!response.ok) {
        throw new Error(`Failed to fetch {feature}: ${response.status}`);
    }

    return response.json();
}
```

**Rules:**
- Never touch the DOM inside an API module.
- Always check `response.ok`. Never silently swallow failed responses.
- Use `async/await` — not raw `.then()` chains.
- Throw errors; let the caller (orchestrator) decide how to handle them.

---

### The Component Module

Reference: [featureCard.js](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/resources/static/js/components/featureCard.js)

One file per UI widget. Accepts data objects and a container; builds and mounts DOM.

```js
// js/components/{feature}Card.js

function create{Feature}Card(item) {
    const card = document.createElement('div');
    card.className = '{feature}-card';
    // ... build the card DOM here

    return card;
}

export function render{Feature}List(container, items) {
    if (!container) return;

    container.innerHTML = '';

    if (!items || items.length === 0) {
        container.textContent = 'Nothing to display.';
        return;
    }

    items.forEach(item => container.appendChild(create{Feature}Card(item)));
}
```

**Rules:**
- Never `fetch` inside a component. Accept data as a function parameter.
- Never reference DOM IDs globally. Accept a `container` argument so the component can be reused anywhere.
- Always handle the empty state gracefully (don't leave the container blank).

---

## 4. Layer Responsibility Rules

| Layer | Can it `fetch`? | Can it touch DOM? | Can it hold logic? |
|---|---|---|---|
| **Model** (Record) | ✗ | ✗ | ✗ |
| **Service** | ✗ | ✗ | ✅ |
| **Controller** | ✗ | ✗ | ✗ |
| **API module** (`js/api/`) | ✅ | ✗ | ✗ |
| **Component** (`js/components/`) | ✗ | ✅ | minimal |
| **Orchestrator** (`main.js`) | ✗ | ✗ | minimal |

> [!IMPORTANT]
> Violating these boundaries is the leading cause of tangled, hard-to-test code. If you find yourself fetching data inside a component, or rendering HTML inside a service, stop and refactor.

---

## 5. Full Project Structure Reference

```
Renz Mapa - Resume/
├── documentation/
│   ├── boilerplate.md          ← This file
│   ├── profile.md
│   └── setup-guide.md
├── src/
│   └── main/
│       ├── java/com/renzmapa/resume_api/
│       │   ├── config/
│       │   │   └── WebConfig.java          Global API prefix config
│       │   ├── profile/                    Feature package
│       │   │   ├── Profile.java
│       │   │   └── ProfileController.java
│       │   └── {feature}/                  ← New features go here
│       │       ├── {Feature}.java
│       │       ├── {Feature}Service.java
│       │       └── {Feature}Controller.java
│       └── resources/
│           └── static/
│               ├── css/
│               │   ├── global.css
│               │   └── {feature}.css
│               ├── images/
│               ├── js/
│               │   ├── api/
│               │   │   ├── profileApi.js
│               │   │   └── {feature}Api.js ← New API modules go here
│               │   ├── components/
│               │   │   ├── socialButtons.js
│               │   │   └── {feature}Card.js ← New components go here
│               │   └── main.js
│               └── index.html
└── pom.xml
```
