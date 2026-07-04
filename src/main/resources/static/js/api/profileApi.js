/**
 * API Service for fetching profile information.
 */

/**
 * Fetches the most recently created profile from the API.
 * Returns a single profile object (not an array).
 *
 * @returns {Promise<Object>} the latest profile data
 */
export async function fetchProfileData() {
    const response = await fetch('/api/profile/latest');
    if (!response.ok) {
        throw new Error(`Failed to fetch profile: ${response.status} ${response.statusText}`);
    }
    return response.json();
}
