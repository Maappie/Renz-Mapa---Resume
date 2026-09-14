/**
 * typewriter.js — UI Component
 *
 * Responsibility: cycle a list of role titles in the hero with a
 * type-and-delete effect. Pure presentation.
 */

const TYPE_MS = 58;
const DELETE_MS = 28;
const HOLD_MS = 1900;
const GAP_MS = 320;

/**
 * Starts the rotating typewriter on an element.
 *
 * When the visitor prefers reduced motion, the first phrase is written once
 * and no animation runs.
 *
 * @param {HTMLElement} target - element whose text is animated
 * @param {string[]} phrases - phrases to cycle through
 */
export function initTypewriter(target, phrases) {
    if (!target) return;

    const list = (phrases || []).filter(Boolean);
    if (list.length === 0) return;

    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches || list.length === 1) {
        target.textContent = list[0];
        return;
    }

    let phraseIndex = 0;
    let charIndex = 0;
    let deleting = false;

    const tick = () => {
        const phrase = list[phraseIndex];

        if (deleting) {
            charIndex -= 1;
            target.textContent = phrase.slice(0, charIndex);

            if (charIndex === 0) {
                deleting = false;
                phraseIndex = (phraseIndex + 1) % list.length;
                window.setTimeout(tick, GAP_MS);
                return;
            }

            window.setTimeout(tick, DELETE_MS);
            return;
        }

        charIndex += 1;
        target.textContent = phrase.slice(0, charIndex);

        if (charIndex === phrase.length) {
            deleting = true;
            window.setTimeout(tick, HOLD_MS);
            return;
        }

        window.setTimeout(tick, TYPE_MS);
    };

    target.textContent = '';
    window.setTimeout(tick, GAP_MS);
}
