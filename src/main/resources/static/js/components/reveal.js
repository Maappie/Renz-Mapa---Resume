/**
 * reveal.js — UI Component
 *
 * Responsibility: fade elements marked `.reveal` into view as they are
 * scrolled to. Elements rendered later by other components are picked up by
 * calling observeReveals() again — already-observed nodes are skipped.
 *
 * Failure policy: content must never be permanently invisible. The hidden
 * state is applied only under the `.js-reveal` class set below, so if this
 * module never runs the page renders fully visible. If it does run but the
 * IntersectionObserver never delivers a callback, the failsafe timer below
 * shows everything anyway.
 */

const STAGGER_CLASSES = ['reveal--d1', 'reveal--d2', 'reveal--d3'];
const FAILSAFE_MS = 1400;

let observer = null;
let observerHasFired = false;
let failsafeTimer = null;

/** Elements being watched that have not been revealed yet. */
const watched = new Set();

// Opt the page into the hidden-until-revealed state. Without this class the
// CSS leaves `.reveal` elements fully visible.
document.documentElement.classList.add('js-reveal');

/**
 * Reveals one element and stops tracking it.
 *
 * @param {HTMLElement} target
 */
function show(target) {
    target.classList.add('is-visible');
    watched.delete(target);
}

/**
 * Lazily creates the shared IntersectionObserver.
 *
 * @returns {IntersectionObserver} the observer instance
 */
function getObserver() {
    if (observer) return observer;

    observer = new IntersectionObserver((entries, self) => {
        observerHasFired = true;
        entries.forEach(entry => {
            if (!entry.isIntersecting) return;
            show(entry.target);
            self.unobserve(entry.target);
        });
    }, { rootMargin: '0px 0px -8% 0px', threshold: 0.08 });

    return observer;
}

/**
 * Arms a one-shot safety net: if the observer has not produced a single
 * callback by the time it fires, every watched element is shown outright.
 * A healthy observer leaves this a no-op.
 */
function armFailsafe() {
    if (failsafeTimer !== null) return;

    failsafeTimer = window.setTimeout(() => {
        if (observerHasFired) return;
        watched.forEach(target => target.classList.add('is-visible'));
        watched.clear();
    }, FAILSAFE_MS);
}

/**
 * Observes every not-yet-observed `.reveal` element inside the root.
 * Elements within the same parent get a small staggered delay so grids
 * cascade instead of appearing all at once.
 *
 * Shows everything immediately when the visitor prefers reduced motion or the
 * browser has no IntersectionObserver.
 *
 * @param {HTMLElement|Document} root - element to search within
 */
export function observeReveals(root) {
    if (!root) return;

    const targets = root.querySelectorAll('.reveal:not([data-reveal-bound])');
    if (targets.length === 0) return;

    const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    const supported = 'IntersectionObserver' in window;

    targets.forEach((target, index) => {
        target.dataset.revealBound = 'true';

        if (reducedMotion || !supported) {
            target.classList.add('is-visible');
            return;
        }

        // Position 0 animates immediately; 1-3 get progressively longer delays.
        const position = index % (STAGGER_CLASSES.length + 1);
        if (position > 0) target.classList.add(STAGGER_CLASSES[position - 1]);

        watched.add(target);
        getObserver().observe(target);
    });

    if (!reducedMotion && supported) armFailsafe();
}
