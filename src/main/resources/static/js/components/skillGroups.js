/**
 * skillGroups.js — UI Component
 *
 * Responsibility: render skill categories returned by /api/skills/grouped.
 * The grouping itself is done server-side — this only draws it.
 */

import { el, icon, chipRow } from './dom.js';

/**
 * Presentation mapping for known categories. Colour and icon are a styling
 * concern, so they live here rather than in the database.
 */
const CATEGORY_STYLE = {
    'Programming Languages': { icon: 'code-2',   accent: 'violet' },
    'Frameworks':            { icon: 'layers',   accent: 'cyan' },
    'Tools & Platforms':     { icon: 'wrench',   accent: 'lime' },
    'AI Tools':              { icon: 'sparkles', accent: 'pink' }
};

const FALLBACK_STYLE = { icon: 'box', accent: 'violet' };

/**
 * Builds one skill category card.
 *
 * @param {{category: string, items: string[]}} group
 * @returns {HTMLElement} the card element
 */
function createGroupCard(group) {
    const style = CATEGORY_STYLE[group.category] || FALLBACK_STYLE;
    const items = group.items || [];

    const card = el('article', 'card skill-group reveal');
    card.dataset.accent = style.accent;

    const head = el('header', 'skill-group__head');
    const iconWrap = el('span', 'skill-group__icon');
    iconWrap.appendChild(icon(style.icon));
    head.appendChild(iconWrap);
    head.appendChild(el('h3', 'skill-group__title', group.category));
    head.appendChild(el('span', 'skill-group__count', String(items.length).padStart(2, '0')));

    card.appendChild(head);
    card.appendChild(chipRow(items));
    return card;
}

/**
 * Renders all skill groups into a container, replacing its contents.
 *
 * @param {HTMLElement} container - element to mount into
 * @param {Array<{category: string, items: string[]}>} groups - grouped skills
 */
export function renderSkillGroups(container, groups) {
    if (!container) return;

    container.innerHTML = '';

    if (!groups || groups.length === 0) {
        container.appendChild(el('p', 'state-msg', 'No skills listed yet.'));
        return;
    }

    groups.forEach(group => container.appendChild(createGroupCard(group)));
}

/**
 * Writes the total skill count into a stat element, e.g. "22".
 *
 * @param {HTMLElement} target - the stat value element
 * @param {Array<{items: string[]}>} groups - grouped skills
 */
export function renderSkillCount(target, groups) {
    if (!target || !groups) return;

    const total = groups.reduce((sum, group) => sum + (group.items ? group.items.length : 0), 0);
    if (total > 0) target.textContent = `${total}`;
}
