/**
 * experienceApi.js — API layer
 *
 * Responsibility: talk to /api/experience and nothing else.
 * No DOM access, no rendering, no formatting.
 */

/**
 * Fetches every work experience entry, ordered by sortOrder ascending.
 *
 * @returns {Promise<Object[]>} array of experience objects
 * @throws {Error} when the request fails or the server returns a non-2xx status
 */
export async function fetchExperience() {
    const response = await fetch('/api/experience');
    if (!response.ok) {
        throw new Error(`Failed to fetch experience: ${response.status} ${response.statusText}`);
    }
    return response.json();
}
