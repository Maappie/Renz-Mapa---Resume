/**
 * profileHeader.js — UI Component
 *
 * Responsibility: write profile data into the static shell of the page —
 * hero, about card and contact panel.
 *
 * Rules:
 *  - No fetching. The profile object is handed in by main.js.
 *  - The root element is a parameter, so this can be mounted anywhere.
 */

/**
 * Sets an element's text, leaving the existing fallback in place when the
 * value is missing.
 *
 * @param {HTMLElement|null} el
 * @param {string} [value]
 */
function setText(el, value) {
    if (el && value) el.textContent = value;
}

/**
 * Turns an element into a working link, or hides the action if there is no URL.
 *
 * @param {HTMLAnchorElement|null} el
 * @param {string} [href]
 * @param {boolean} [external=false] - open in a new tab when true
 */
function setLink(el, href, external = false) {
    if (!el) return;

    if (!href) {
        el.removeAttribute('href');
        return;
    }

    el.href = href;
    if (external) {
        el.target = '_blank';
        el.rel = 'noopener noreferrer';
    }
}

/**
 * Strips spaces and dashes so a display phone number works as a tel: link.
 *
 * @param {string} phone
 * @returns {string} dial-safe number
 */
function toDialable(phone) {
    return phone.replace(/[^\d+]/g, '');
}

/**
 * Populates every profile-driven element inside the given root.
 *
 * @param {HTMLElement|Document} root - element containing the profile markup
 * @param {Object} profile - profile object from /api/profile/latest
 */
export function renderProfile(root, profile) {
    if (!root || !profile) return;

    // Hero + about copy
    setText(root.querySelector('#profile-name'), profile.name);
    setText(root.querySelector('#profile-bio'), profile.bio);
    setText(root.querySelector('#about-bio'), profile.bio);
    setText(root.querySelector('#profile-location'), profile.location);
    setText(root.querySelector('#profile-location-2'), profile.location);

    // Email — about card + contact panel
    if (profile.email) {
        const mailto = `mailto:${profile.email}`;
        setText(root.querySelector('#profile-email'), profile.email);
        setLink(root.querySelector('#profile-email'), mailto);
        setText(root.querySelector('#contact-email-text'), profile.email);
        setLink(root.querySelector('#contact-email-link'), mailto);
    }

    // Phone — about card + contact panel
    if (profile.phone) {
        const tel = `tel:${toDialable(profile.phone)}`;
        setText(root.querySelector('#profile-phone'), profile.phone);
        setLink(root.querySelector('#profile-phone'), tel);
        setText(root.querySelector('#contact-phone-text'), profile.phone);
        setLink(root.querySelector('#contact-phone-link'), tel);
    }

    // GitHub — contact panel shows the handle rather than the full URL
    if (profile.github) {
        const handle = profile.github.replace(/\/+$/, '').split('/').pop();
        setText(root.querySelector('#contact-github-text'), handle ? `@${handle}` : profile.github);
        setLink(root.querySelector('#contact-github-link'), profile.github, true);
    }
}
