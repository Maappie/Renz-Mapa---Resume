/**
 * featureApi.js — API Service Layer
 *
 * Responsibility: All network communication for the "feature" resource.
 *
 * Rules:
 *  - Never import DOM APIs here. This module is purely data transport.
 *  - Never handle UI state here. Throw errors and let callers decide.
 *  - Keep the base URL in one place so it's trivial to change environments.
 */

const BASE_URL = '/api/features';

/**
 * Fetches all features from the REST API.
 *
 * @returns {Promise<Object[]>} Resolves with an array of feature objects.
 * @throws {Error} If the network response is not OK.
 */
export async function fetchAllFeatures() {
    const response = await fetch(BASE_URL);

    if (!response.ok) {
        throw new Error(`Failed to fetch features: ${response.status} ${response.statusText}`);
    }

    return response.json();
}

/**
 * Fetches a single feature by its ID.
 *
 * @param {number|string} id - The unique identifier of the feature.
 * @returns {Promise<Object>} Resolves with a single feature object.
 * @throws {Error} If the resource is not found (404) or other HTTP error.
 */
export async function fetchFeatureById(id) {
    const response = await fetch(`${BASE_URL}/${id}`);

    if (response.status === 404) {
        throw new Error(`Feature with id "${id}" was not found.`);
    }

    if (!response.ok) {
        throw new Error(`Failed to fetch feature ${id}: ${response.status} ${response.statusText}`);
    }

    return response.json();
}
