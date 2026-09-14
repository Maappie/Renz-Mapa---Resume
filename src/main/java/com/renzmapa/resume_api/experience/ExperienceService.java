package com.renzmapa.resume_api.experience;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

    public ExperienceService(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    /**
     * Returns every experience entry, ordered by sortOrder ascending.
     * This is the order the portfolio timeline renders them in.
     *
     * @return list of all Experience entities, oldest sortOrder first
     */
    public List<Experience> getAll() {
        return experienceRepository.findAllByOrderBySortOrderAsc();
    }

    /**
     * Returns a single experience entry by its ID.
     *
     * @param id the experience ID
     * @return the Experience, or null if not found
     */
    public Experience getById(Long id) {
        return experienceRepository.findById(id).orElse(null);
    }

    /**
     * Creates a new experience row in the database.
     *
     * @param request DTO containing the experience fields to set
     * @return the newly created Experience with its generated ID
     */
    public Experience create(ExperienceRequest request) {
        Experience experience = new Experience(
            request.getCompany(),
            request.getRole(),
            request.getPeriod(),
            request.getCurrent() != null && request.getCurrent(),
            request.getSummary(),
            request.getHighlights(),
            request.getTags(),
            request.getSortOrder()
        );
        return experienceRepository.save(experience);
    }

    /**
     * Partially updates an Experience by ID.
     * Only fields that are non-null in the request will overwrite the stored value.
     * Null fields in the request are ignored — the existing value is kept.
     *
     * @param id      ID of the experience to update
     * @param request DTO containing the fields to change (any subset is valid)
     * @return the updated Experience, or null if the ID was not found
     */
    public Experience patch(Long id, ExperienceRequest request) {
        Experience existing = experienceRepository.findById(id).orElse(null);
        if (existing == null) return null;

        return applyPatch(existing, request);
    }

    /**
     * Deletes an experience entry by ID.
     *
     * @param id ID of the experience to delete
     * @return true if the row existed and was deleted, false if the ID was not found
     */
    public boolean delete(Long id) {
        if (!experienceRepository.existsById(id)) return false;

        experienceRepository.deleteById(id);
        return true;
    }

    /**
     * Applies non-null fields from the request to an existing entity and saves it.
     */
    private Experience applyPatch(Experience existing, ExperienceRequest request) {
        if (request.getCompany()     != null) existing.setCompany(request.getCompany());
        if (request.getRole()        != null) existing.setRole(request.getRole());
        if (request.getPeriod()      != null) existing.setPeriod(request.getPeriod());
        if (request.getCurrent()     != null) existing.setCurrent(request.getCurrent());
        if (request.getSummary()     != null) existing.setSummary(request.getSummary());
        if (request.getHighlights()  != null) existing.setHighlights(request.getHighlights());
        if (request.getTags()        != null) existing.setTags(request.getTags());
        if (request.getSortOrder()   != null) existing.setSortOrder(request.getSortOrder());

        return experienceRepository.save(existing);
    }
}
