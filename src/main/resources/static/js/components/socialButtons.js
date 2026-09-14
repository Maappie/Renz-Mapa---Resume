/**
 * socialButtons.js — UI Component
 *
 * Responsibility: build the social icon buttons from a list of URLs.
 * No fetching — main.js passes the links in.
 */

/** Maps a URL to its Lucide icon name and accessible label. */
const SOCIAL_MAP = [
    { match: 'github.com',   icon: 'github',        label: 'GitHub' },
    { match: 'linkedin.com', icon: 'linkedin',      label: 'LinkedIn' },
    { match: 'facebook.com', icon: 'facebook',      label: 'Facebook' },
    { match: 'twitter.com',  icon: 'twitter',       label: 'Twitter' },
    { match: 'x.com',        icon: 'twitter',       label: 'X' },
    { match: 'instagram.com', icon: 'instagram',    label: 'Instagram' },
    { match: 'youtube.com',  icon: 'youtube',       label: 'YouTube' }
];

/**
 * Resolves the icon + label for a social URL.
 *
 * @param {string} url
 * @returns {{icon: string, label: string}} icon name and accessible label
 */
function describeUrl(url) {
    const found = SOCIAL_MAP.find(entry => url.includes(entry.match));
    return found || { icon: 'external-link', label: 'Website' };
}

/**
 * Renders social buttons into a container, replacing whatever was there.
 *
 * @param {HTMLElement} container - element to mount the buttons into
 * @param {string[]} links - social profile URLs (falsy entries are skipped)
 */
export function renderSocialLinks(container, links) {
    if (!container) return;

    const urls = (links || []).filter(Boolean);
    container.innerHTML = '';

    if (urls.length === 0) {
        const note = document.createElement('p');
        note.className = 'state-msg';
        note.textContent = 'No social links yet.';
        container.appendChild(note);
        return;
    }

    urls.forEach(url => {
        const { icon, label } = describeUrl(url);

        const button = document.createElement('a');
        button.className = 'social-btn';
        button.href = url;
        button.target = '_blank';
        button.rel = 'noopener noreferrer';
        button.setAttribute('aria-label', label);
        button.title = label;

        const glyph = document.createElement('i');
        glyph.setAttribute('data-lucide', icon);
        button.appendChild(glyph);

        container.appendChild(button);
    });
}
