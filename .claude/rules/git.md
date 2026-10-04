# Git — Commits and Branches

## When to commit

- Only commit or push when the user asks. Making a change is not permission to commit it.
- Never force-push, rewrite published history, or skip hooks (`--no-verify`) unless explicitly told.
- Before committing, run the Definition of Done checks in `workflow.md`. Do not commit a red build.

## Commit messages — Conventional Commits

The history follows Conventional Commits. Keep it that way:

```
<type>(<scope>): <imperative, lowercase summary, no trailing period>
```

| Type       | Use for                                               |
|------------|-------------------------------------------------------|
| `feat`     | New behaviour visible to a user or API consumer       |
| `fix`      | Bug fix                                               |
| `refactor` | Code change with no behaviour change                  |
| `test`     | Adding or fixing tests only                           |
| `docs`     | Documentation, CLAUDE.md, rules                       |
| `chore`    | Build, config, gitignore, data files, dependencies    |
| `style`    | Formatting only (no logic)                            |

**Scope** is the feature slice or area: `profile`, `project`, `skill`, `experience`, `education`,
`feature`, `ui`, `db`, `docs`, `config`, `admin`. Omit it when a change is genuinely cross-cutting.

Examples from this repo's history:

```
feat(project): add project feature slice
feat(ui): add frontend api and component modules
chore(db): add resume content for the new feature slices
```

- Summary line ≤ 72 characters. Add a body (after a blank line) when the *why* is not obvious.

## Commit hygiene

- **One logical change per commit.** A new slice and an unrelated CSS tweak are two commits.
- Stage files by name; never `git add .` / `git add -A` blindly — check `git status` first.
- Never commit secrets, `.env` files, `target/`, `bin/`, IDE folders, or PDFs.
- `resume.db` is tracked. Only stage it when the commit is *intentionally* changing resume content,
  and use a `chore(db): ...` commit for it — never bundle it into a code commit.
