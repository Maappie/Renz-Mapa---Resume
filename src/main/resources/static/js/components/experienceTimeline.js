/**
 * experienceTimeline.js — UI Component
 *
 * Responsibility: draw the work-experience timeline from /api/experience data.
 * No fetching, no sorting — the API already returns entries in display order.
 */

import { el, chipRow, bulletList } from './dom.js';

/**
 * Builds one timeline entry.
 *
 * @param {Object} entry - experience object from the API
 * @returns {HTMLElement} an <li> timeline item
 */
function createTimelineItem(entry) {
    const item = el('li', 'card tl-item reveal');
    if (entry.current) item.classList.add('tl-item--current');

    item.appendChild(el('span', 'tl-item__node'));

    const head = el('div', 'tl-item__head');
    head.appendChild(el('h3', 'tl-item__role', entry.role));
    if (entry.current) head.appendChild(el('span', 'badge-now', 'Now'));
    item.appendChild(head);

    const meta = el('div', 'tl-item__meta');
    meta.appendChild(el('span', 'tl-item__company', entry.company));
    meta.appendChild(el('span', 'tl-item__period', entry.period));
    item.appendChild(meta);

    if (entry.summary) item.appendChild(el('p', 'tl-item__summary', entry.summary));
    if (entry.highlights && entry.highlights.length > 0) item.appendChild(bulletList(entry.highlights));
    if (entry.tags && entry.tags.length > 0) item.appendChild(chipRow(entry.tags));

    return item;
}

/**
 * Renders the full timeline into a container, replacing its contents.
 *
 * @param {HTMLElement} container - the <ol class="timeline"> element
 * @param {Object[]} entries - experience objects from the API
 */
export function renderTimeline(container, entries) {
    if (!container) return;

    container.innerHTML = '';

    if (!entries || entries.length === 0) {
        container.appendChild(el('p', 'state-msg', 'No experience entries yet.'));
        return;
    }

    entries.forEach(entry => container.appendChild(createTimelineItem(entry)));
}
