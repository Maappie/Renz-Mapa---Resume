package com.renzmapa.resume_api.skill;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    /**
     * Returns all skills as a flat list in display order (lowest sortOrder first).
     *
     * @return ordered list of all Skill entities
     */
    public List<Skill> getAll() {
        return skillRepository.findAllByOrderBySortOrderAsc();
    }

    /**
     * Returns all skills bundled by category.
     * This is what the portfolio frontend uses to render the skills section.
     *
     * <p>Order is preserved end to end: skills arrive sorted by sortOrder and a
     * LinkedHashMap keeps first-seen insertion order, so each category lands at the
     * position of the lowest sortOrder it contains, and the names inside stay in order.
     *
     * @return list of categories, each with its skill names in display order
     */
    public List<SkillGroupResponse> getGrouped() {
        Map<String, List<String>> grouped = new LinkedHashMap<>();

        for (Skill skill : getAll()) {
            grouped.computeIfAbsent(skill.getCategory(), category -> new ArrayList<>())
                   .add(skill.getName());
        }

        return grouped.entrySet().stream()
            .map(entry -> new SkillGroupResponse(entry.getKey(), entry.getValue()))
            .toList();
    }

    /**
     * Returns a single skill by its ID.
     *
     * @param id the skill ID
     * @return the Skill, or null if not found
     */
    public Skill getById(Long id) {
        return skillRepository.findById(id).orElse(null);
    }

    /**
     * Creates a new skill row in the database.
     *
     * @param request DTO containing the skill fields to set
     * @return the newly created Skill with its generated ID
     */
    public Skill create(SkillRequest request) {
        Skill skill = new Skill(
            request.getCategory(),
            request.getName(),
            request.getSortOrder()
        );
        return skillRepository.save(skill);
    }

    /**
     * Partially updates a Skill by ID.
     * Only fields that are non-null in the request will overwrite the stored value.
     * Null fields in the request are ignored — the existing value is kept.
     *
     * @param id      ID of the skill to update
     * @param request DTO containing the fields to change (any subset is valid)
     * @return the updated Skill, or null if the ID was not found
     */
    public Skill patch(Long id, SkillRequest request) {
        Skill existing = skillRepository.findById(id).orElse(null);
        if (existing == null) return null;

        return applyPatch(existing, request);
    }

    /**
     * Deletes a skill by ID.
     *
     * @param id the skill ID to remove
     * @return true if a row was deleted, false if the ID was not found
     */
    public boolean delete(Long id) {
        if (!skillRepository.existsById(id)) return false;

        skillRepository.deleteById(id);
        return true;
    }

    /**
     * Applies non-null fields from the request to an existing entity and saves it.
     */
    private Skill applyPatch(Skill existing, SkillRequest request) {
        if (request.getCategory()    != null) existing.setCategory(request.getCategory());
        if (request.getName()        != null) existing.setName(request.getName());
        if (request.getSortOrder()   != null) existing.setSortOrder(request.getSortOrder());

        return skillRepository.save(existing);
    }
}
