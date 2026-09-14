/**
 * skillApi.js — API layer
 *
 * Responsibility: talk to /api/skills and nothing else.
 * No DOM access, no rendering, no formatting.
 */

/**
 * Fetches skills already grouped by category, in display order.
 * The grouping is done server-side (SkillService) so the frontend
 * never has to reshape the data itself.
 *
 * @returns {Promise<Array<{category: string, items: string[]}>>} grouped skills
 * @throws {Error} when the request fails or the server returns a non-2xx status
 */
export async function fetchGroupedSkills() {
    const response = await fetch('/api/skills/grouped');
    if (!response.ok) {
        throw new Error(`Failed to fetch skills: ${response.status} ${response.statusText}`);
    }
    return response.json();
}
