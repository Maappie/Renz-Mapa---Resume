/**
 * educationApi.js — API layer
 *
 * Responsibility: talk to /api/education and nothing else.
 * No DOM access, no rendering, no formatting.
 */

/**
 * Fetches every education entry, ordered by sortOrder ascending.
 *
 * @returns {Promise<Object[]>} array of education objects
 * @throws {Error} when the request fails or the server returns a non-2xx status
 */
export async function fetchEducation() {
    const response = await fetch('/api/education');
    if (!response.ok) {
        throw new Error(`Failed to fetch education: ${response.status} ${response.statusText}`);
    }
    return response.json();
}
