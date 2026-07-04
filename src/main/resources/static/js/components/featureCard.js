/**
 * featureCard.js — UI Component
 *
 * Responsibility: Build and return DOM elements for a feature.
 *
 * Rules:
 *  - This module only creates DOM — it does NOT fetch data.
 *  - Exported functions accept data objects and a container element as arguments.
 *  - Never reference global IDs here. Accept containers as parameters
 *    so this component can be mounted anywhere on the page.
 */

/**
 * Creates a single feature card DOM element.
 *
 * @param {Object} feature - A feature data object from the API.
 * @param {string} feature.name - Display name of the feature.
 * @param {string} feature.description - Short description.
 * @param {string[]} [feature.tags=[]] - Optional list of tag strings.
 * @returns {HTMLElement} A fully constructed card element, ready to append.
 */
function createFeatureCard(feature) {
    const card = document.createElement('div');
    card.className = 'feature-card';
    card.setAttribute('data-id', feature.id);

    const name = document.createElement('h3');
    name.className = 'feature-card__name';
    name.textContent = feature.name;

    const description = document.createElement('p');
    description.className = 'feature-card__description';
    description.textContent = feature.description;

    const tagList = document.createElement('ul');
    tagList.className = 'feature-card__tags';

    (feature.tags || []).forEach(tag => {
        const tagItem = document.createElement('li');
        tagItem.className = 'feature-card__tag';
        tagItem.textContent = tag;
        tagList.appendChild(tagItem);
    });

    card.appendChild(name);
    card.appendChild(description);
    card.appendChild(tagList);

    return card;
}

/**
 * Renders a list of features into a given container element.
 *
 * Clears the container before rendering to prevent duplicate entries.
 *
 * @param {HTMLElement} container - The DOM element to mount cards into.
 * @param {Object[]} features - Array of feature objects from the API.
 */
export function renderFeatureList(container, features) {
    if (!container) {
        console.warn('[featureCard] renderFeatureList: container is null or undefined.');
        return;
    }

    container.innerHTML = '';

    if (!features || features.length === 0) {
        const empty = document.createElement('p');
        empty.className = 'feature-card--empty';
        empty.textContent = 'No features available.';
        container.appendChild(empty);
        return;
    }

    features.forEach(feature => {
        const card = createFeatureCard(feature);
        container.appendChild(card);
    });
}
