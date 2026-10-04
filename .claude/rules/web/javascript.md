---
paths:
  - "src/main/resources/static/**/*.js"
  - "src/main/resources/static/**/*.html"
---

# Frontend JavaScript

Vanilla ES modules, no build step, no framework. Do not introduce a framework, bundler, or npm
dependency without asking.

## Organise by responsibility

```
static/js/
├── api/          ← all fetch() calls. One module per backend resource: {feature}Api.js
├── components/   ← DOM rendering. One module per UI piece: {thing}.js (camelCase)
└── main.js       ← entry point: wires api/ → components/; no fetch, no DOM construction
```

| Module         | Allowed                                                     | Forbidden                                   |
|----------------|-------------------------------------------------------------|---------------------------------------------|
| `api/*.js`     | `fetch`, checking `response.ok`, returning parsed JSON, throwing | DOM access, formatting, rendering        |
| `components/*.js` | Building/updating DOM from the data it is given; event listeners for its own UI | `fetch`, calling `api/`, business rules (filtering, sorting, deciding what data means) |
| `main.js`      | Importing api + components, orchestrating load order, error handling per section | `fetch`, `document.createElement` chains |

- **No inline `<script>` blocks** with logic in HTML. Pages load one module: `<script type="module" src="/js/main.js">`.
- `api-explorer.js` is a standalone tool page whose purpose is sending arbitrary requests; it is the
  only accepted exception to the fetch rule. Do not copy its pattern elsewhere.

## API modules

Follow `projectApi.js` exactly:

```js
export async function fetchProjects() {
    const response = await fetch('/api/projects');
    if (!response.ok) {
        throw new Error(`Failed to fetch projects: ${response.status} ${response.statusText}`);
    }
    return response.json();
}
```

- Exported functions are `async`, named `fetchX` / `createX` / `updateX` / `deleteX`.
- Always check `response.ok` and throw a descriptive `Error`. Never return `undefined` on failure.
- Use relative `/api/...` paths — never hardcode `http://localhost:8080`.
- Send JSON with `headers: { 'Content-Type': 'application/json' }` and `JSON.stringify(body)`.
- A `204` response has no body — do not call `response.json()` on it.

## Components

- Signature: `render{Thing}(container, data)` — accept data, mutate the container. Pure builders
  (`create{Thing}(item)`) return an element.
- Build DOM with the helpers in `components/dom.js` (`el`, `icon`, `chipRow`, `bulletList`). Add a
  helper there instead of duplicating element-building code.
- **Never set `innerHTML` with data.** Use `textContent`. `container.innerHTML = ''` to clear is fine.
- Handle empty data deliberately (render a state message or nothing — never a broken layout).
- Call `refreshIcons()` after mounting new Lucide placeholders.

## Language and style

- `const` by default, `let` only when reassigned, never `var`.
- `async/await`, not `.then()` chains.
- Wrap top-level awaits in `try/catch`; on failure log with a context prefix
  (`console.error('[portfolio] ...', error)`) **and** show the user a readable state message.
  One failing section must not blank the page (see `loadSection` in `main.js`).
- ES module `import`/`export` only; named exports, no default exports; include the `.js` extension
  in import paths.
- Constants in `SCREAMING_SNAKE_CASE` at the top of the module (`FALLBACK_ICON`).
- Functions ≤ ~20 lines; extract helpers as `createProjectCard` / `createLinkRow` do.
- No `console.log` left in committed code; `console.error`/`console.warn` for real failures only.
- No global variables; no mutation of `window` except a deliberate, documented hook.
- Every file starts with a header comment stating its responsibility; every exported function has
  JSDoc (`@param`, `@returns`, `@throws`).
