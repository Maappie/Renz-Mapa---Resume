/**
 * marquee.js — UI Component
 *
 * Responsibility: fill the scrolling tech ticker.
 *
 * The item list is rendered twice back-to-back: the CSS animation travels
 * exactly -50% of the track, so the second copy lands where the first began
 * and the loop has no visible seam.
 */

import { el } from './dom.js';

/**
 * Renders the marquee items into the track element.
 *
 * @param {HTMLElement} track - the .marquee__track element
 * @param {string[]} items - labels to scroll, e.g. the profile tech stack
 */
export function renderMarquee(track, items) {
    if (!track) return;

    const labels = (items || []).filter(Boolean);
    track.innerHTML = '';

    if (labels.length === 0) return;

    labels.concat(labels).forEach(label => {
        track.appendChild(el('span', 'marquee__item', label));
    });
}
