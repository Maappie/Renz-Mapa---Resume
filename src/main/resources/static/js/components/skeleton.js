/**
 * skeleton.js — UI Component
 *
 * Responsibility: placeholder and empty/error states shown while (or instead of)
 * real content. Pure DOM — never fetches, never decides when to appear.
 */

/**
 * Fills a container with shimmering placeholder blocks.
 *
 * @param {HTMLElement} container - element to fill
 * @param {number} count - how many placeholders to render
 * @param {string} [modifier='skeleton--card'] - size modifier class
 */
export function renderSkeletons(container, count, modifier = 'skeleton--card') {
    if (!container) return;

    container.innerHTML = '';
    for (let i = 0; i < count; i += 1) {
        const block = document.createElement('div');
        block.className = `skeleton ${modifier}`;
        container.appendChild(block);
    }
}

/**
 * Replaces a container's contents with a single message — used for both
 * "nothing here yet" and "couldn't load this" states.
 *
 * @param {HTMLElement} container - element to fill
 * @param {string} message - text to display
 */
export function renderStateMessage(container, message) {
    if (!container) return;

    container.innerHTML = '';
    const note = document.createElement('p');
    note.className = 'state-msg';
    note.textContent = message;
    container.appendChild(note);
}
