/**
 * projectApi.js — API layer
 *
 * Responsibility: talk to /api/projects and nothing else.
 * No DOM access, no rendering, no formatting.
 */

/**
 * Fetches every project, ordered by sortOrder ascending.
 *
 * @returns {Promise<Object[]>} array of project objects
 * @throws {Error} when the request fails or the server returns a non-2xx status
 */
export async function fetchProjects() {
    const response = await fetch('/api/projects');
    if (!response.ok) {
        throw new Error(`Failed to fetch projects: ${response.status} ${response.statusText}`);
    }
    return response.json();
}
