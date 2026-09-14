/**
 * main.js — Application entry point
 *
 * Responsibility: wire API modules to UI components and start page behaviours.
 *
 * Rules followed here:
 *  - No fetch() in this file. Every request goes through js/api/*.
 *  - No DOM construction in this file. Every element is built in js/components/*.
 *  - Each section loads independently, so one failing endpoint cannot blank the page.
 */

import { fetchProfileData } from './api/profileApi.js';
import { fetchGroupedSkills } from './api/skillApi.js';
import { fetchExperience } from './api/experienceApi.js';
import { fetchProjects } from './api/projectApi.js';
import { fetchEducation } from './api/educationApi.js';

import { renderProfile } from './components/profileHeader.js';
import { renderSocialLinks } from './components/socialButtons.js';
import { renderMarquee } from './components/marquee.js';
import { renderSkillGroups, renderSkillCount } from './components/skillGroups.js';
import { renderTimeline } from './components/experienceTimeline.js';
import { renderProjects, renderProjectCount } from './components/projectGrid.js';
import { renderEducation } from './components/educationCard.js';
import { renderSkeletons, renderStateMessage } from './components/skeleton.js';
import { initNavbar, initActiveLinks, initBackToTop } from './components/navbar.js';
import { observeReveals } from './components/reveal.js';
import { initTypewriter } from './components/typewriter.js';
import { initContactForm } from './components/contactForm.js';
import { refreshIcons } from './components/icons.js';

/** Roles cycled in the hero when the profile has no title of its own. */
const FALLBACK_ROLES = [
    'Software Developer',
    'Full-Stack Developer',
    'Automation Engineer',
    'Computer Engineer'
];

/**
 * Builds the hero rotator list, leading with the title stored on the profile.
 *
 * @param {string} [title] - profile title from the API
 * @returns {string[]} roles to cycle through
 */
function buildRoles(title) {
    if (!title) return FALLBACK_ROLES;
    return [title, ...FALLBACK_ROLES.filter(role => role !== title)];
}

/**
 * Loads one API-backed section: skeleton → fetch → render, with a readable
 * message if the request fails.
 *
 * @param {HTMLElement} container - element the component renders into
 * @param {Function} fetcher - API function returning a promise of the data
 * @param {Function} render - component function (container, data) => void
 * @param {Object} [options] - skeleton count/modifier and error copy
 * @returns {Promise<*|null>} the fetched data, or null when the request failed
 */
async function loadSection(container, fetcher, render, options = {}) {
    if (!container) return null;

    const {
        skeletonCount = 3,
        skeletonModifier = 'skeleton--card',
        errorMessage = 'Couldn\'t load this section — the API may be offline.'
    } = options;

    renderSkeletons(container, skeletonCount, skeletonModifier);

    try {
        const data = await fetcher();
        render(container, data);
        return data;
    } catch (error) {
        console.error('[portfolio] section failed to load:', error);
        renderStateMessage(container, errorMessage);
        return null;
    } finally {
        container.removeAttribute('data-loading');
        observeReveals(document);
        refreshIcons();
    }
}

/**
 * Loads the profile and everything driven by it: hero copy, contact details,
 * social buttons, the tech marquee, the role rotator and the contact form.
 */
async function loadProfile() {
    const socialsEl = document.querySelector('#socials');
    const rotatorEl = document.querySelector('#role-rotator');
    const formEl = document.querySelector('#contact-form');
    const marqueeEl = document.querySelector('#marquee-track');

    try {
        const profile = await fetchProfileData();

        renderProfile(document, profile);
        renderSocialLinks(socialsEl, [profile.github, profile.linkedin, profile.facebook]);
        renderMarquee(marqueeEl, profile.stack);
        initTypewriter(rotatorEl, buildRoles(profile.title));
        initContactForm(formEl, profile.email || null);
    } catch (error) {
        console.error('[portfolio] profile failed to load:', error);
        renderStateMessage(socialsEl, 'Links unavailable.');
        initTypewriter(rotatorEl, FALLBACK_ROLES);
        initContactForm(formEl, null);
    } finally {
        refreshIcons();
    }
}

/** Loads skills and updates the skill-count stat tile. */
async function loadSkills() {
    const groups = await loadSection(
        document.querySelector('#skill-groups'),
        fetchGroupedSkills,
        renderSkillGroups,
        { skeletonCount: 4, errorMessage: 'Couldn\'t load the stack right now.' }
    );
    if (groups) renderSkillCount(document.querySelector('#stat-skills'), groups);
}

/** Loads projects and updates the project-count stat tile. */
async function loadProjects() {
    const projects = await loadSection(
        document.querySelector('#project-grid'),
        fetchProjects,
        renderProjects,
        { skeletonCount: 3, errorMessage: 'Couldn\'t load projects right now.' }
    );
    if (projects) renderProjectCount(document.querySelector('#stat-projects'), projects);
}

/** Boots page chrome, then loads every section in parallel. */
async function init() {
    initNavbar(document);
    initActiveLinks(document);
    initBackToTop(document);
    observeReveals(document);
    refreshIcons();

    await Promise.allSettled([
        loadProfile(),
        loadSkills(),
        loadProjects(),
        loadSection(
            document.querySelector('#timeline'),
            fetchExperience,
            renderTimeline,
            { skeletonCount: 2, skeletonModifier: 'skeleton--row', errorMessage: 'Couldn\'t load experience right now.' }
        ),
        loadSection(
            document.querySelector('#education-list'),
            fetchEducation,
            renderEducation,
            { skeletonCount: 1, skeletonModifier: 'skeleton--row', errorMessage: 'Couldn\'t load education right now.' }
        )
    ]);

    observeReveals(document);
    refreshIcons();
}

document.addEventListener('DOMContentLoaded', init);
