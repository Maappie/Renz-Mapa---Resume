/**
 * icons.js — UI Component helper
 *
 * Responsibility: turn <i data-lucide="..."> placeholders into real SVGs.
 *
 * Lucide replaces the placeholder elements in-place, so this has to run
 * again every time a component injects new markup into the page.
 */

/**
 * Re-scans the document and renders any un-rendered Lucide icon placeholders.
 * Safe to call repeatedly, and a no-op if the Lucide script failed to load.
 */
export function refreshIcons() {
    if (window.lucide && typeof window.lucide.createIcons === 'function') {
        window.lucide.createIcons();
    }
}
