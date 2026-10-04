# Documentation — Keeping Docs True

## When docs must change in the same task

| You changed…                                   | Update…                                            |
|------------------------------------------------|----------------------------------------------------|
| An entity's fields or a new table              | `documentation/model.md`                           |
| The shape of a feature slice / scaffolding     | `documentation/boilerplate.md`                     |
| Profile behaviour or the settings page         | `documentation/profile.md`, `profile-settings-guide.md` |
| Commands, ports, prerequisites, project layout | `documentation/setup.md` (and `setup-guide.md`)    |
| An engineering rule                            | The matching file in `.claude/rules/` — nowhere else |

A doc that describes code that no longer exists is a bug. If you can't update it, say so.

## Rules are single-sourced

- Engineering rules live **only** in `.claude/rules/`. `CLAUDE.md` and `.agents/AGENTS.md` point at
  them; they must not restate or contradict them.
- To change a rule, edit its file and mention the change in your reply.

## Code comments

- **Javadoc on every public service and controller method** — what it does, `@param`, `@return`, and
  what "not found" looks like (`null`, `false`, empty list).
- Class-level Javadoc explains the class's *role and any non-obvious decision* (see `ProjectRequest`
  explaining why `featured` is a boxed `Boolean`).
- Inline comments explain **why**, never **what**. Delete comments that restate the code.
- JS: every exported function gets a JSDoc block with `@param` / `@returns` / `@throws`; every file
  starts with a header comment naming its single responsibility (see `projectApi.js`).
- No commented-out code. No `TODO` without a linked issue or a stated owner/date.
