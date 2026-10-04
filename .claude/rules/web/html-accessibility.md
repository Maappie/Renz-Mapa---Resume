---
paths:
  - "src/main/resources/static/**/*.html"
  - "src/main/resources/static/js/components/**"
---

# HTML and Accessibility

## Markup

- Every page: `<!DOCTYPE html>`, `<html lang="en">`, `<meta charset="UTF-8">`, the viewport meta,
  a unique `<title>`, and a `<meta name="description">`.
- HTML is **structure only**: no inline styles, no inline event handlers (`onclick=`), no inline
  `<script>` logic. Behaviour is attached from JS modules.
- Use semantic elements: `<header>`, `<nav>`, `<main>` (exactly one), `<section>` with a heading,
  `<article>` for self-contained cards, `<footer>`, `<button>` for actions, `<a>` for navigation.
  Never a clickable `<div>`.
- Heading levels go in order (`h1` → `h2` → `h3`) with one `h1` per page.
- Elements that JS renders into get a stable `id` or `data-` hook. Prefer selecting by those over
  styling classes, so a CSS rename cannot silently break behaviour.
- Asset paths are root-relative: `/css/...`, `/js/...`, `/images/...`.

## Accessibility (WCAG 2.1 AA as the bar)

- Every `<img>` has `alt` — descriptive, or `alt=""` if purely decorative.
- Icon-only buttons and links have an `aria-label`. Decorative icons get `aria-hidden="true"`.
- Every form control has an associated `<label>` (or `aria-label`); errors are announced in text,
  not by colour alone.
- Everything interactive is reachable and operable by keyboard, with a visible focus style
  (`:focus-visible`). Do not remove outlines without a replacement.
- Text colour tokens must keep ≥ 4.5:1 contrast against their background (3:1 for large text).
- Loading and error states rendered by JS should be perceivable: use `aria-live="polite"` on
  regions whose content is replaced after load.

## Third-party resources

- External scripts and styles are pinned to an exact version
  (`https://unpkg.com/lucide@0.380.0`, never `@latest`).
- Only Google Fonts and the existing Lucide CDN are in use. Adding another external resource is a
  dependency change — ask first.
- External links that open a new tab use `target="_blank" rel="noopener noreferrer"`.
