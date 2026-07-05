import { fetchProfileData } from './api/profileApi.js';

/**
 * Populates all portfolio DOM elements with profile data from the API.
 * @param {Object} data
 */
function populateProfileDOM(data) {
    // Hero
    const nameEl = document.getElementById('name');
    if (nameEl) nameEl.textContent = data.name || 'Renz Mapa';

    const titleEl = document.getElementById('title');
    if (titleEl) titleEl.textContent = data.title || '';

    const bioEl = document.getElementById('hero-bio') || document.getElementById('bio');
    if (bioEl) bioEl.textContent = data.bio || '';

    // About info
    const locationEl = document.getElementById('location');
    if (locationEl) locationEl.textContent = data.location || '—';

    const emailEl = document.getElementById('email');
    if (emailEl) emailEl.textContent = data.email || '—';

    const phoneEl = document.getElementById('phone');
    if (phoneEl) phoneEl.textContent = data.phone || '—';

    // Socials
    const githubBtn = document.getElementById('social-github');
    if (githubBtn) {
        githubBtn.href = data.github || '#';
        githubBtn.target = data.github ? '_blank' : '';
        githubBtn.rel = data.github ? 'noopener noreferrer' : '';
    }

    const linkedinBtn = document.getElementById('social-linkedin');
    if (linkedinBtn) {
        linkedinBtn.href = data.linkedin || '#';
        linkedinBtn.target = data.linkedin ? '_blank' : '';
        linkedinBtn.rel = data.linkedin ? 'noopener noreferrer' : '';
    }

    const facebookBtn = document.getElementById('social-facebook');
    if (facebookBtn) {
        facebookBtn.href = data.facebook || '#';
        facebookBtn.target = data.facebook ? '_blank' : '';
        facebookBtn.rel = data.facebook ? 'noopener noreferrer' : '';
    }

    // Skills & Tools
    const skillsContainer = document.getElementById('skills');
    if (skillsContainer && data.stack) {
        skillsContainer.innerHTML = ''; // Clear fallback/static tags
        data.stack.forEach(tech => {
            const span = document.createElement('span');
            span.className = 'skill-tag';
            span.textContent = tech;
            skillsContainer.appendChild(span);
        });
    }

    // Re-initialise Lucide Icons after dynamic DOM changes
    if (window.lucide) {
        window.lucide.createIcons();
    }
}

// ─── Navbar scroll effect ────────────────────────────────────
function initNavbar() {
    const navbar = document.getElementById('navbar');
    if (!navbar) return;
    window.addEventListener('scroll', () => {
        navbar.classList.toggle('scrolled', window.scrollY > 20);
    }, { passive: true });
}

// ─── Initialise Application ──────────────────────────────────
document.addEventListener('DOMContentLoaded', async () => {
    initNavbar();

    // Initialise Lucide Icons for static icons rendered in HTML
    if (window.lucide) {
        window.lucide.createIcons();
    }

    try {
        const profileData = await fetchProfileData();
        populateProfileDOM(profileData);
    } catch (error) {
        console.error('Error fetching profile data:', error);
        const nameEl = document.getElementById('name');
        if (nameEl) nameEl.textContent = 'Renz Mapa';
    }
});
