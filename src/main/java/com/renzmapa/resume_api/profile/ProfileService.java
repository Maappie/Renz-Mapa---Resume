package com.renzmapa.resume_api.profile;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Returns all profiles in the database.
     *
     * @return list of all Profile entities
     */
    public List<Profile> getAll() {
        return profileRepository.findAll();
    }

    /**
     * Returns a single profile by its ID.
     *
     * @param id the profile ID
     * @return the Profile, or null if not found
     */
    public Profile getById(Long id) {
        return profileRepository.findById(id).orElse(null);
    }

    /**
     * Returns the most recently created profile (highest ID).
     * This is what the portfolio frontend uses to display data.
     *
     * @return the latest Profile, or null if the table is empty
     */
    public Profile getLatest() {
        return profileRepository.findTopByOrderByIdDesc().orElse(null);
    }

    /**
     * Creates a new profile row in the database.
     * This is the "update by adding" approach — previous profiles are preserved as history.
     *
     * @param request DTO containing the profile fields to set
     * @return the newly created Profile with its generated ID
     */
    public Profile create(ProfileUpdateRequest request) {
        Profile profile = new Profile(
            request.getName(),
            request.getTitle(),
            request.getEmail(),
            request.getPhone(),
            request.getLocation(),
            request.getBio(),
            request.getGithub(),
            request.getLinkedin(),
            request.getFacebook(),
            request.getStack()
        );
        return profileRepository.save(profile);
    }

    /**
     * Partially updates a Profile by ID.
     * Only fields that are non-null in the request will overwrite the stored value.
     * Null fields in the request are ignored — the existing value is kept.
     *
     * @param id      ID of the profile to update
     * @param request DTO containing the fields to change (any subset is valid)
     * @return the updated Profile, or null if the ID was not found
     */
    public Profile patch(Long id, ProfileUpdateRequest request) {
        Profile existing = profileRepository.findById(id).orElse(null);
        if (existing == null) return null;

        return applyPatch(existing, request);
    }

    /**
     * Partially updates the latest profile (highest ID).
     * Convenience method so callers don't need to know the ID.
     *
     * @param request DTO containing the fields to change
     * @return the updated Profile, or null if no profiles exist
     */
    public Profile patchLatest(ProfileUpdateRequest request) {
        Profile existing = profileRepository.findTopByOrderByIdDesc().orElse(null);
        if (existing == null) return null;

        return applyPatch(existing, request);
    }

    /**
     * Applies non-null fields from the request to an existing entity and saves it.
     */
    private Profile applyPatch(Profile existing, ProfileUpdateRequest request) {
        if (request.getName()        != null) existing.setName(request.getName());
        if (request.getTitle()       != null) existing.setTitle(request.getTitle());
        if (request.getEmail()       != null) existing.setEmail(request.getEmail());
        if (request.getPhone()       != null) existing.setPhone(request.getPhone());
        if (request.getLocation()    != null) existing.setLocation(request.getLocation());
        if (request.getBio()         != null) existing.setBio(request.getBio());
        if (request.getGithub()      != null) existing.setGithub(request.getGithub());
        if (request.getLinkedin()    != null) existing.setLinkedin(request.getLinkedin());
        if (request.getFacebook()    != null) existing.setFacebook(request.getFacebook());
        if (request.getStack()       != null) existing.setStack(request.getStack());

        return profileRepository.save(existing);
    }
}
