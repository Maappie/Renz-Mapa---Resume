/**
 * projectGrid.js — UI Component
 *
 * Responsibility: draw project cards from /api/projects data.
 * Each project carries its own `icon` and `accent` from the API, so the
 * owner controls a project's look without touching this file.
 */

import { el, icon, chipRow, bulletList } from './dom.js';

const FALLBACK_ICON = 'folder-git-2';
const FALLBACK_ACCENT = 'violet';

/**
 * Builds the optional "Source" / "Live demo" link row.
 * Returns null when a project has neither link, so no dead anchors are rendered.
 *
 * @param {Object} project
 * @returns {HTMLElement|null} the link row, or null
 */
function createLinkRow(project) {
    if (!project.repoUrl && !project.demoUrl) return null;

    const row = el('div', 'project__links');

    if (project.demoUrl) {
        const demo = el('a', 'project__link');
        demo.href = project.demoUrl;
        demo.target = '_blank';
        demo.rel = 'noopener noreferrer';
        demo.appendChild(icon('external-link'));
        demo.appendChild(el('span', null, 'Live demo'));
        row.appendChild(demo);
    }

    if (project.repoUrl) {
        const repo = el('a', 'project__link');
        repo.href = project.repoUrl;
        repo.target = '_blank';
        repo.rel = 'noopener noreferrer';
        repo.appendChild(icon('github'));
        repo.appendChild(el('span', null, 'Source'));
        row.appendChild(repo);
    }

    return row;
}

/**
 * Builds one project card.
 *
 * @param {Object} project - project object from the API
 * @returns {HTMLElement} the card element
 */
function createProjectCard(project) {
    const card = el('article', 'card project reveal');
    card.dataset.accent = project.accent || FALLBACK_ACCENT;

    const top = el('div', 'project__top');
    const iconWrap = el('span', 'project__icon');
    iconWrap.appendChild(icon(project.icon || FALLBACK_ICON));
    top.appendChild(iconWrap);

    const topMeta = el('div', 'project__top-meta');
    if (project.featured) topMeta.appendChild(el('span', 'badge-featured', 'Featured'));
    topMeta.appendChild(el('span', 'project__year', project.year));
    top.appendChild(topMeta);
    card.appendChild(top);

    card.appendChild(el('h3', 'project__name', project.name));
    if (project.role) card.appendChild(el('p', 'project__role', project.role));
    if (project.blurb) card.appendChild(el('p', 'project__blurb', project.blurb));
    if (project.highlights && project.highlights.length > 0) card.appendChild(bulletList(project.highlights));
    if (project.tags && project.tags.length > 0) card.appendChild(chipRow(project.tags, 'chip-row project__tags'));

    const links = createLinkRow(project);
    if (links) card.appendChild(links);

    return card;
}

/**
 * Renders all project cards into a container, replacing its contents.
 *
 * @param {HTMLElement} container - element to mount into
 * @param {Object[]} projects - project objects from the API
 */
export function renderProjects(container, projects) {
    if (!container) return;

    container.innerHTML = '';

    if (!projects || projects.length === 0) {
        container.appendChild(el('p', 'state-msg', 'No projects yet.'));
        return;
    }

    projects.forEach(project => container.appendChild(createProjectCard(project)));
}

/**
 * Writes the project count into a stat element.
 *
 * @param {HTMLElement} target - the stat value element
 * @param {Object[]} projects - project objects from the API
 */
export function renderProjectCount(target, projects) {
    if (!target || !projects || projects.length === 0) return;
    target.textContent = `${projects.length}`;
}
