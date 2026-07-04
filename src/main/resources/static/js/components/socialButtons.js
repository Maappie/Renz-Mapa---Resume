/**
 * Assigns an icon based on URL keywords.
 * @param {string} url 
 * @returns {string} Lucide icon name
 */
function getIconForUrl(url) {
    if (url.includes('github.com')) {
        return 'github';
    }
    if (url.includes('linkedin.com')) {
        return 'linkedin';
    }
    if (url.includes('twitter.com') || url.includes('x.com')) {
        return 'twitter';
    }
    return 'external-link';
}

/**
 * Dynamically builds and inserts social media buttons into a container.
 * @param {HTMLElement} container - The container element to append buttons to
 * @param {string[]} socialLinks - Array of social link URLs
 */
export function renderSocialLinks(container, socialLinks) {
    if (!container) return;

    container.innerHTML = ''; // Clear fallback buttons

    socialLinks.forEach(link => {
        const btn = document.createElement('a');
        btn.href = link;
        btn.target = '_blank';
        btn.rel = 'noopener noreferrer';
        btn.className = 'social-btn';

        const iconName = getIconForUrl(link);
        btn.innerHTML = `<i data-lucide="${iconName}"></i>`;
        container.appendChild(btn);
    });
}
