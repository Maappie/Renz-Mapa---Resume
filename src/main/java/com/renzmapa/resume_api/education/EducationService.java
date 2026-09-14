package com.renzmapa.resume_api.education;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EducationService {

    private final EducationRepository educationRepository;

    public EducationService(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    /**
     * Returns every education entry, ordered by sortOrder ascending.
     * This is the order the portfolio renders them in.
     *
     * @return list of all Education entities, lowest sortOrder first
     */
    public List<Education> getAll() {
        return educationRepository.findAllByOrderBySortOrderAsc();
    }

    /**
     * Returns a single education entry by its ID.
     *
     * @param id the education ID
     * @return the Education, or null if not found
     */
    public Education getById(Long id) {
        return educationRepository.findById(id).orElse(null);
    }

    /**
     * Creates a new education row in the database.
     *
     * @param request DTO containing the education fields to set
     * @return the newly created Education with its generated ID
     */
    public Education create(EducationRequest request) {
        Education education = new Education(
            request.getSchool(),
            request.getDegree(),
            request.getHonors(),
            request.getPeriod(),
            request.getDetails(),
            request.getSortOrder()
        );
        return educationRepository.save(education);
    }

    /**
     * Partially updates an Education by ID.
     * Only fields that are non-null in the request will overwrite the stored value.
     * Null fields in the request are ignored — the existing value is kept.
     *
     * @param id      ID of the education entry to update
     * @param request DTO containing the fields to change (any subset is valid)
     * @return the updated Education, or null if the ID was not found
     */
    public Education patch(Long id, EducationRequest request) {
        Education existing = educationRepository.findById(id).orElse(null);
        if (existing == null) return null;

        return applyPatch(existing, request);
    }

    /**
     * Deletes an education entry by ID.
     *
     * @param id ID of the education entry to delete
     * @return true if the row existed and was deleted, false if the ID was not found
     */
    public boolean delete(Long id) {
        if (!educationRepository.existsById(id)) return false;

        educationRepository.deleteById(id);
        return true;
    }

    /**
     * Applies non-null fields from the request to an existing entity and saves it.
     */
    private Education applyPatch(Education existing, EducationRequest request) {
        if (request.getSchool()      != null) existing.setSchool(request.getSchool());
        if (request.getDegree()      != null) existing.setDegree(request.getDegree());
        if (request.getHonors()      != null) existing.setHonors(request.getHonors());
        if (request.getPeriod()      != null) existing.setPeriod(request.getPeriod());
        if (request.getDetails()     != null) existing.setDetails(request.getDetails());
        if (request.getSortOrder()   != null) existing.setSortOrder(request.getSortOrder());

        return educationRepository.save(existing);
    }
}
