/**
 * educationCard.js — UI Component
 *
 * Responsibility: draw education entries from /api/education data.
 */

import { el, icon } from './dom.js';

/**
 * Builds one education card.
 *
 * @param {Object} entry - education object from the API
 * @returns {HTMLElement} the card element
 */
function createEducationCard(entry) {
    const card = el('article', 'card edu reveal');

    const iconWrap = el('span', 'edu__icon');
    iconWrap.appendChild(icon('graduation-cap'));
    card.appendChild(iconWrap);

    const body = el('div', 'edu__body');

    const head = el('div', 'edu__head');
    head.appendChild(el('h3', 'edu__degree', entry.degree));
    if (entry.honors) {
        const badge = el('span', 'badge-honors');
        badge.appendChild(icon('award'));
        badge.appendChild(el('span', null, entry.honors));
        head.appendChild(badge);
    }
    body.appendChild(head);

    if (entry.school) body.appendChild(el('p', 'edu__school', entry.school));
    if (entry.period) body.appendChild(el('p', 'edu__period', entry.period));
    if (entry.details) body.appendChild(el('p', 'edu__details', entry.details));

    card.appendChild(body);
    return card;
}

/**
 * Renders all education entries into a container, replacing its contents.
 *
 * @param {HTMLElement} container - element to mount into
 * @param {Object[]} entries - education objects from the API
 */
export function renderEducation(container, entries) {
    if (!container) return;

    container.innerHTML = '';

    if (!entries || entries.length === 0) {
        container.appendChild(el('p', 'state-msg', 'No education entries yet.'));
        return;
    }

    entries.forEach(entry => container.appendChild(createEducationCard(entry)));
}
