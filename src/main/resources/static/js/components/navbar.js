/**
 * navbar.js — UI Component
 *
 * Responsibility: page chrome behaviour — the sticky navbar's scrolled state,
 * the mobile menu, section-aware link highlighting, and the back-to-top button.
 * No fetching, no data.
 */

import { icon } from './dom.js';
import { refreshIcons } from './icons.js';

const SCROLLED_AFTER_PX = 16;
const TO_TOP_AFTER_PX = 600;
const TABLET_BREAKPOINT = 900;

/**
 * Swaps the hamburger glyph between "menu" and "x".
 *
 * @param {HTMLElement} toggle - the toggle button
 * @param {boolean} isOpen - whether the menu is currently open
 */
function setToggleIcon(toggle, isOpen) {
    toggle.innerHTML = '';
    toggle.appendChild(icon(isOpen ? 'x' : 'menu'));
    toggle.setAttribute('aria-expanded', String(isOpen));
    toggle.setAttribute('aria-label', isOpen ? 'Close menu' : 'Open menu');
    refreshIcons();
}

/**
 * Wires the navbar: scrolled styling and the mobile menu.
 *
 * @param {HTMLElement|Document} root - element containing the navbar markup
 */
export function initNavbar(root) {
    const nav = root.querySelector('#nav');
    const toggle = root.querySelector('#nav-toggle');
    const menu = root.querySelector('#nav-menu');
    if (!nav) return;

    window.addEventListener('scroll', () => {
        nav.classList.toggle('is-scrolled', window.scrollY > SCROLLED_AFTER_PX);
    }, { passive: true });

    if (!toggle || !menu) return;

    const closeMenu = () => {
        menu.classList.remove('is-open');
        setToggleIcon(toggle, false);
    };

    toggle.addEventListener('click', () => {
        const willOpen = !menu.classList.contains('is-open');
        menu.classList.toggle('is-open', willOpen);
        setToggleIcon(toggle, willOpen);
    });

    menu.querySelectorAll('a').forEach(link => link.addEventListener('click', closeMenu));

    document.addEventListener('keydown', event => {
        if (event.key === 'Escape') closeMenu();
    });

    window.addEventListener('resize', () => {
        if (window.innerWidth > TABLET_BREAKPOINT) closeMenu();
    });
}

/**
 * Highlights the nav link whose section is currently in view.
 *
 * @param {HTMLElement|Document} root - element containing the navbar markup
 */
export function initActiveLinks(root) {
    const links = Array.from(root.querySelectorAll('.nav__link'));
    if (links.length === 0) return;

    const sections = links
        .map(link => {
            const section = root.querySelector(link.getAttribute('href'));
            return section ? { link, section } : null;
        })
        .filter(Boolean);

    const observer = new IntersectionObserver(entries => {
        entries.forEach(entry => {
            if (!entry.isIntersecting) return;
            const match = sections.find(pair => pair.section === entry.target);
            if (!match) return;
            links.forEach(link => link.classList.toggle('is-active', link === match.link));
        });
    }, { rootMargin: '-45% 0px -50% 0px', threshold: 0 });

    sections.forEach(pair => observer.observe(pair.section));
}

/**
 * Wires the floating back-to-top button.
 *
 * @param {HTMLElement|Document} root - element containing the button
 */
export function initBackToTop(root) {
    const button = root.querySelector('#to-top');
    if (!button) return;

    window.addEventListener('scroll', () => {
        button.classList.toggle('is-visible', window.scrollY > TO_TOP_AFTER_PX);
    }, { passive: true });

    button.addEventListener('click', () => {
        const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
        window.scrollTo({ top: 0, behavior: reducedMotion ? 'auto' : 'smooth' });
    });
}
