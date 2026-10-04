# Security — Cross-Cutting Rules

## Secrets

- Never hardcode credentials, API keys, tokens, or passwords in code, `application.properties`,
  HTML, or JS. Read them from environment variables: `${MY_SECRET}` in properties.
- Never commit `.env` files or anything containing a real secret. If one is found in the diff, stop
  and tell the user.
- Never log secrets, tokens, or full request bodies that may contain personal data.

## Input handling (backend)

- Treat every request body, path variable, and query parameter as untrusted.
- Validate values with a closed set of allowed options (e.g. `accent` ∈ violet | cyan | lime | pink)
  in the service layer before saving. Reject invalid input with `400 Bad Request` — do not store it.
- Use Spring Data derived queries or `@Query` with bound parameters. Never build JPQL/SQL by string
  concatenation with user input.
- URLs stored for the frontend (`repoUrl`, `demoUrl`, social links) must be `http`/`https` only —
  reject `javascript:` and other schemes.

## Output handling (frontend)

- Render API data with `textContent` (via the helpers in `js/components/dom.js`), never `innerHTML`
  with data. `innerHTML = ''` to clear a container is fine.
- External links opened in a new tab must use `rel="noopener noreferrer"`.
- Third-party scripts must be pinned to an exact version (`lucide@0.380.0`, not `lucide@latest`).

## Exposed surface

- `application.properties` exposes all actuator endpoints and full health details. That is
  acceptable for local development only. Any change that prepares the app for deployment must
  restrict actuator exposure and protect `/admin` — flag this to the user rather than shipping it open.
- Do not add new endpoints that mutate data without considering who can call them; mention the lack
  of authentication when adding write endpoints.
