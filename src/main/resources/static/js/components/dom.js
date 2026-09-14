/**
 * dom.js — UI Component helpers
 *
 * Responsibility: small builders shared by the rendering components so each
 * one stays readable. Everything here uses textContent, never innerHTML with
 * data, so API values can never inject markup.
 */

/**
 * Creates an element with an optional class and text content.
 *
 * @param {string} tag - tag name
 * @param {string} [className] - class attribute
 * @param {string} [text] - text content
 * @returns {HTMLElement} the new element
 */
export function el(tag, className, text) {
    const node = document.createElement(tag);
    if (className) node.className = className;
    if (text !== undefined && text !== null && text !== '') node.textContent = text;
    return node;
}

/**
 * Creates a Lucide icon placeholder. Call refreshIcons() after mounting.
 *
 * @param {string} name - Lucide icon name, e.g. "github"
 * @returns {HTMLElement} an <i data-lucide="..."> placeholder
 */
export function icon(name) {
    const node = document.createElement('i');
    node.setAttribute('data-lucide', name);
    return node;
}

/**
 * Builds a row of pill-shaped tag chips.
 *
 * @param {string[]} items - chip labels
 * @param {string} [className='chip-row'] - class for the wrapper
 * @returns {HTMLElement} the wrapper element (empty if there are no items)
 */
export function chipRow(items, className = 'chip-row') {
    const row = el('div', className);
    (items || []).forEach(item => row.appendChild(el('span', 'chip', item)));
    return row;
}

/**
 * Builds a bulleted list of achievement lines.
 *
 * @param {string[]} items - bullet text
 * @returns {HTMLElement} a <ul class="bullets"> element
 */
export function bulletList(items) {
    const list = el('ul', 'bullets');
    (items || []).forEach(item => list.appendChild(el('li', null, item)));
    return list;
}
