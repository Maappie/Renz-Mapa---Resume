---
paths:
  - "src/main/resources/static/**/*.css"
  - "src/main/resources/static/**/*.html"
---

# CSS Architecture

## Design tokens live in `global.css` only

- Every colour, gradient, shadow/glow, font family, font size, spacing step, radius, and motion
  duration/easing is a custom property under `:root` in `css/global.css`.
- Page stylesheets **consume** tokens: `color: var(--text-secondary); padding: var(--space-4);`
- **No hex, `rgb()`, `rgba()`, or `hsl()` literals outside `global.css`.** Need a new colour or
  translucent wash? Add a token to `global.css` (follow the existing groups: Surfaces, Text, Accents,
  `--{accent}-soft`, `--{accent}-line`, Gradients, Glows) and use it.
- Spacing, radii, and font sizes use tokens (`--space-*`, `--radius-*`, `--fs-*`). Raw `px` values
  are acceptable only for: hairline borders (`1px`), media-query breakpoints (CSS variables cannot be
  used there), and one-off geometry with no design meaning (an icon's box size). If a raw value
  repeats, it should be a token.
- Do not redefine a token in a page stylesheet.

## One stylesheet per page

- `global.css` — tokens, reset, base element styles, shared primitives (`.card`, `.chip`).
- `{page}.css` — scoped to one page: `profile.css` (index), `settings.css`, `api-explorer.css`.
- Each page links `global.css` first, then its own sheet. A new page gets a new sheet.
- Styles used by two or more pages move to `global.css`.

## Naming — BEM

- Block, element, modifier: `.project`, `.project__links`, `.project__link`, `.skeleton--card`.
- State via modifier or data attribute: `.is-open` / `[data-accent="lime"]`. The accent keys from the
  API (`violet | cyan | lime | pink`) map to tokens through `[data-accent]` selectors — never through
  colours computed in JS.
- Do not style by ID or by bare tag inside page sheets; keep specificity low and flat (≤ 3 levels).
- No `!important` except to honour `prefers-reduced-motion`.

## Layout and responsiveness

- Mobile-friendly by default; layouts use flex/grid, not absolute positioning hacks.
- **Breakpoints are exactly `900px` (tablet) and `640px` (mobile)**, written as
  `@media (max-width: 900px)` and `@media (max-width: 640px)`. No other breakpoint values.
- Every animation/transition has a `@media (prefers-reduced-motion: reduce)` fallback.

## Never

- No inline `style=""` in HTML, and no `element.style.x = ...` in JS for anything a class can do.
  Toggle classes or data attributes instead.
- No CSS-in-JS, no new CSS framework (Tailwind, Bootstrap) without asking.
