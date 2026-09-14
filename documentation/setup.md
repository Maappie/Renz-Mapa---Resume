# Local Setup

Everything needed to get this project running on a fresh machine: prerequisites, commands,
URLs, project layout, and the things that commonly go wrong.

> **TL;DR** — install Java 21, then run `./mvnw spring-boot:run` (Windows: `.\mvnw.cmd spring-boot:run`)
> and open <http://localhost:8080>. There is no database to install and no frontend build step.

---

## 1. What you are running

A single Spring Boot application that does two jobs at once:

- **REST API** (`/api/**`) — profile, experience, projects, skills and education data, stored in SQLite.
- **Static frontend** (`/`) — the portfolio page, plus two admin pages. Plain HTML/CSS/JS served
  straight from `src/main/resources/static`. No React, no bundler, no `npm install`.

One process, one port, no external services.

---

## 2. Prerequisites

| Tool | Version | Required? | Notes |
|---|---|---|---|
| **JDK** | **21+** | Yes | The build targets Java 21 (`<java.version>21</java.version>` in `pom.xml`). |
| Git | any | Yes | To clone the repo. |
| Maven | — | **No** | The Maven wrapper (`mvnw`) downloads Maven 3.9.16 on first run. |
| SQLite | — | **No** | The driver is a Java library; the database is just the `resume.db` file. |
| Node.js | — | **No** | Not used by the app. Only needed for the optional browser checks in §11. |

Check your Java version:

```bash
java -version
```

You want `21` or higher. If you have multiple JDKs installed, make sure `JAVA_HOME` points at 21:

```powershell
# Windows (PowerShell)
$env:JAVA_HOME
```

```bash
# macOS / Linux
echo $JAVA_HOME
```

An older JDK fails at compile time with `invalid target release: 21` or
`class file has wrong version`.

---

## 3. First run

```bash
git clone <repo-url>
cd "Renz Mapa - Resume"

# Windows (PowerShell / CMD)
.\mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

The first run downloads Maven and all dependencies (a few minutes, needs internet). Later runs
start in a few seconds.

When you see `Started ResumeApiApplication`, open <http://localhost:8080>.

**Nothing else to set up** — no `.env`, no credentials, no database creation step. On first boot
Hibernate creates any missing tables and the seeders insert the resume content.

---

## 4. Commands

Run these from the project root. Windows users: use `.\mvnw.cmd` wherever `./mvnw` appears.

| Command | What it does |
|---|---|
| `./mvnw spring-boot:run` | Run the app in dev mode on port 8080. Ctrl+C to stop. |
| `./mvnw test` | Run the test suite (currently the Spring context-load test). |
| `./mvnw clean compile` | Compile only — fastest way to check for Java errors. |
| `./mvnw clean package` | Build the executable JAR into `target/`. |
| `java -jar target/resume-api-0.0.1-SNAPSHOT.jar` | Run the packaged JAR (no Maven needed). |
| `./mvnw dependency:tree` | Show the full dependency graph. |

### Running from an IDE

- **VS Code** — open the project root, then run `ResumeApiApplication.java`
  ([src/main/java/com/renzmapa/resume_api/ResumeApiApplication.java](../src/main/java/com/renzmapa/resume_api/ResumeApiApplication.java))
  with the Extension Pack for Java installed.
- **IntelliJ IDEA** — open `pom.xml` as a project, then run the same class.

---

## 5. URLs

| URL | What it is |
|---|---|
| <http://localhost:8080/> | **The portfolio** — the main page. |
| <http://localhost:8080/admin/api-docs> | **Swagger UI** — browse and edit data through the API. Give it ~5 seconds to render. |
| <http://localhost:8080/api-explorer.html> | Custom API explorer page. |
| <http://localhost:8080/settings.html> | Profile settings admin page (see the known issue in §10). |
| <http://localhost:8080/admin> | Spring Boot Admin — health, metrics, beans, environment. |
| <http://localhost:8080/actuator/health> | Raw health endpoint. |
| <http://localhost:8080/api/admin/v3/api-docs/0-all> | Raw OpenAPI JSON. |

> **Why the odd `/api/admin/...` path for the JSON?** [`WebConfig`](../src/main/java/com/renzmapa/resume_api/config/WebConfig.java)
> prefixes `/api` onto *every* `@RestController`, and springdoc's spec endpoint is itself a
> `@RestController` — so it inherits the prefix. Swagger UI figures this out on its own; you only
> need this path if you want the raw spec.

---

## 6. API reference

All REST controllers are automatically prefixed with `/api` by `WebConfig`, so controller classes
map `/profile` and the live route is `/api/profile`. **Never hardcode `/api` in a `@RequestMapping`.**

| Method | Path | Purpose |
|---|---|---|
| `GET` | `/api/profile` | All profile versions |
| `GET` | `/api/profile/latest` | ⭐ The profile the homepage reads |
| `GET` `PATCH` | `/api/profile/{id}` | Read / partially update one version |
| `POST` | `/api/profile` | Create a new profile version |
| `PATCH` | `/api/profile/latest` | ⭐ Edit the live profile without knowing its ID |
| `GET` `POST` | `/api/experience` | Work history (ordered by `sortOrder`) |
| `GET` `PATCH` `DELETE` | `/api/experience/{id}` | One experience entry |
| `GET` `POST` | `/api/projects` | Portfolio projects |
| `GET` `PATCH` `DELETE` | `/api/projects/{id}` | One project |
| `GET` `POST` | `/api/skills` | Flat skill list |
| `GET` | `/api/skills/grouped` | ⭐ Skills bundled by category — what the frontend renders |
| `GET` `PATCH` `DELETE` | `/api/skills/{id}` | One skill |
| `GET` `POST` | `/api/education` | Schools and degrees |
| `GET` `PATCH` `DELETE` | `/api/education/{id}` | One education entry |
| `GET` | `/api/features`, `/api/features/{id}` | Demo in-memory feature resource |

`PATCH` accepts any subset of fields — omitted fields keep their current value.

Quick check that the API is alive:

```bash
curl http://localhost:8080/api/profile/latest
curl http://localhost:8080/api/skills/grouped
```

Editing content, e.g. changing your job title:

```bash
curl -X PATCH http://localhost:8080/api/profile/latest \
  -H "Content-Type: application/json" \
  -d '{ "title": "Software Engineer" }'
```

Refresh the portfolio and the change is live — the page reads everything from these endpoints.

---

## 7. Project layout

```
.
├── pom.xml                       Maven build + dependencies
├── mvnw / mvnw.cmd               Maven wrapper (no Maven install needed)
├── resume.db                     SQLite database (committed to git)
├── CLAUDE.md                     Engineering rules entry point
├── .agents/AGENTS.md             The full engineering rulebook
├── documentation/                You are here
└── src/
    ├── main/java/com/renzmapa/resume_api/
    │   ├── ResumeApiApplication.java    Entry point
    │   ├── profile/                     Feature slice
    │   ├── experience/                  Feature slice
    │   ├── project/                     Feature slice
    │   ├── skill/                       Feature slice
    │   ├── education/                   Feature slice
    │   ├── feature/                     Demo slice (in-memory)
    │   ├── config/                      WebConfig (/api prefix), OpenApiConfig (Swagger)
    │   └── admin/                       Spring Boot Admin server config
    ├── main/resources/
    │   ├── application.properties       All configuration
    │   └── static/
    │       ├── index.html               The portfolio page
    │       ├── settings.html            Profile admin page
    │       ├── api-explorer.html        API explorer page
    │       ├── css/
    │       │   ├── global.css           Design tokens — all colours/spacing live here
    │       │   └── profile.css          Portfolio page styles
    │       ├── js/
    │       │   ├── api/                 fetch() calls only
    │       │   ├── components/          DOM rendering only
    │       │   └── main.js              Wires api + components
    │       └── images/                  renz.jpg (headshot)
    └── test/java/                       Tests
```

Each feature slice holds its own entity, repository, service, controller, request DTO and seeder.
See [boilerplate.md](boilerplate.md) for the pattern to copy when adding a feature, and
[../.agents/AGENTS.md](../.agents/AGENTS.md) for the rules that govern all of it.

> `bin/` is an IDE build-output mirror of the project. Ignore it — never edit files there.

---

## 8. How the data works

- **Storage** — SQLite, in the single file `resume.db` at the project root. It is **committed to
  git**, so a fresh clone already contains the resume content.
- **Schema** — managed by Hibernate with `spring.jpa.hibernate.ddl-auto=update`: missing tables and
  columns are created at startup. Nothing is ever dropped.
- **Seed data** — each feature has a `*Seeder` (`CommandLineRunner`) that inserts the resume content
  **only when its table is empty**. Re-running the app never duplicates or overwrites your edits.

### Reset the database

Stop the app, delete the file, restart:

```bash
rm resume.db          # Windows PowerShell: Remove-Item resume.db
./mvnw spring-boot:run
```

Hibernate recreates the tables and the seeders repopulate them from the Java source. Any edits you
made through the API are lost — that is the point of a reset.

### Inspect the database directly

```bash
# needs the sqlite3 CLI, optional
sqlite3 resume.db ".tables"
sqlite3 resume.db "select id, name, year from projects;"
```

---

## 9. Development workflow

| You changed… | What to do |
|---|---|
| HTML, CSS, JS, images in `static/` | **Just refresh the browser.** Served live from source while `spring-boot:run` is running. |
| Java code | Rebuild — devtools restarts the app when `target/classes` changes. In an IDE, save triggers it; from the CLI, restart `spring-boot:run` or run `./mvnw compile` in a second terminal. |
| `application.properties` | Restart the app. |
| `pom.xml` | Restart the app (dependencies are re-resolved on start). |

A hard refresh (`Ctrl+Shift+R`) helps if the browser serves a stale CSS file.

---

## 10. Troubleshooting

**Port 8080 already in use** — something else (often an earlier run of this app) holds the port:

```powershell
# Windows — find and kill it
Get-NetTCPConnection -LocalPort 8080 -State Listen |
  ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }
```

```bash
# macOS / Linux
lsof -ti:8080 | xargs kill -9
```

Or run on a different port: `./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`

**`invalid target release: 21`** — your JDK is older than 21, or `JAVA_HOME` points at an old one. See §2.

**First build hangs or fails downloading** — the wrapper needs internet access to fetch Maven and
dependencies. Behind a proxy, configure it in `~/.m2/settings.xml`.

**Swagger UI sits on "LOADING"** — normal for the first few seconds while it parses 29 operations.
If it never loads, confirm the spec itself responds: `curl http://localhost:8080/api/admin/v3/api-docs/0-all`

**The portfolio loads but sections are empty** — the API isn't answering. Check the browser console
and `curl http://localhost:8080/api/projects`. Each section fails independently by design, so an
empty section means that one endpoint is down, not the whole app.

**`settings.html` hangs on "Loading…"** — known issue: the page requests `/js/settings.js`, which
does not exist in the repo, so nothing populates. Until it is written, edit profile data through
Swagger UI or `curl` (§6). The API Explorer page and the portfolio itself are unaffected.

**SQLite locking errors, or `git` refusing to touch `resume.db`** — the running app holds the file
open. Stop it first (on Windows, `git reset`/`checkout` fails with
`unable to unlink old 'resume.db': Invalid argument` while it is running).

**There is no CV download on the site** — intentional: `.gitignore` excludes `*.pdf`, so no PDF is
committed and a tracked download link would 404 on a fresh clone. To add one, drop a PDF into
`src/main/resources/static/files/`, force-add it (`git add -f`) or add a negation rule to
`.gitignore`, then link to `/files/<name>.pdf` from the hero in `index.html`.

---

## 11. Optional: browser checks

Not needed to run or develop the app — useful when changing the frontend and you want to confirm
layout, console errors and mobile behaviour without clicking around manually.

Requires Node.js. `playwright-core` drives the Chrome you already have installed, so there is no
browser download:

```bash
npm install playwright-core
```

A minimal script that flags console errors and horizontal overflow:

```js
const { chromium } = require('playwright-core');
(async () => {
  const browser = await chromium.launch({ channel: 'chrome' });
  const page = await browser.newPage({ viewport: { width: 390, height: 844 } });
  page.on('pageerror', e => console.log('ERROR', e.message));
  await page.goto('http://localhost:8080/', { waitUntil: 'networkidle' });
  console.log('overflows horizontally:',
    await page.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth));
  await page.screenshot({ path: 'mobile.png', fullPage: true });
  await browser.close();
})();
```

Keep this outside the repo (or in a scratch folder) — the project itself has no Node dependencies.

---

## 12. Tech stack

| Layer | Technology | Version |
|---|---|---|
| Language | Java | 21 |
| Framework | Spring Boot | 4.1.0 |
| Web | Spring MVC (`spring-boot-starter-webmvc`) | — |
| Persistence | Spring Data JPA + Hibernate | — |
| Database | SQLite (`org.xerial:sqlite-jdbc`) | 3.47.1.0 |
| SQL dialect | `hibernate-community-dialects` (`SQLiteDialect`) | — |
| API docs | springdoc-openapi (Swagger UI) | 3.0.3 |
| Monitoring | Spring Boot Actuator + Spring Boot Admin | 4.1.1 |
| Dev tooling | Spring Boot DevTools, Lombok | — |
| Build | Maven (via wrapper) | 3.9.16 |
| Frontend | Vanilla HTML/CSS/JS (ES modules), Lucide icons, Google Fonts | — |

Related reading: [setup-guide.md](setup-guide.md) (short version of §3),
[boilerplate.md](boilerplate.md) (how to add a feature), [model.md](model.md) (data model),
[profile.md](profile.md) and [profile-settings-guide.md](profile-settings-guide.md) (the Profile feature).
