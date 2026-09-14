package com.renzmapa.resume_api.project;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Returns all projects in display order (lowest sortOrder first).
     * This is what the portfolio frontend uses to render the project cards.
     *
     * @return ordered list of all Project entities
     */
    public List<Project> getAll() {
        return projectRepository.findAllByOrderBySortOrderAsc();
    }

    /**
     * Returns a single project by its ID.
     *
     * @param id the project ID
     * @return the Project, or null if not found
     */
    public Project getById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    /**
     * Creates a new project row in the database.
     *
     * @param request DTO containing the project fields to set
     * @return the newly created Project with its generated ID
     */
    public Project create(ProjectRequest request) {
        Project project = new Project(
            request.getName(),
            request.getRole(),
            request.getYear(),
            request.getBlurb(),
            request.getHighlights(),
            request.getTags(),
            request.getIcon(),
            request.getAccent(),
            request.getRepoUrl(),
            request.getDemoUrl(),
            request.getFeatured() != null && request.getFeatured(),
            request.getSortOrder()
        );
        return projectRepository.save(project);
    }

    /**
     * Partially updates a Project by ID.
     * Only fields that are non-null in the request will overwrite the stored value.
     * Null fields in the request are ignored — the existing value is kept.
     *
     * @param id      ID of the project to update
     * @param request DTO containing the fields to change (any subset is valid)
     * @return the updated Project, or null if the ID was not found
     */
    public Project patch(Long id, ProjectRequest request) {
        Project existing = projectRepository.findById(id).orElse(null);
        if (existing == null) return null;

        return applyPatch(existing, request);
    }

    /**
     * Deletes a project by ID.
     *
     * @param id the project ID to remove
     * @return true if a row was deleted, false if the ID was not found
     */
    public boolean delete(Long id) {
        if (!projectRepository.existsById(id)) return false;

        projectRepository.deleteById(id);
        return true;
    }

    /**
     * Applies non-null fields from the request to an existing entity and saves it.
     */
    private Project applyPatch(Project existing, ProjectRequest request) {
        if (request.getName()        != null) existing.setName(request.getName());
        if (request.getRole()        != null) existing.setRole(request.getRole());
        if (request.getYear()        != null) existing.setYear(request.getYear());
        if (request.getBlurb()       != null) existing.setBlurb(request.getBlurb());
        if (request.getHighlights()  != null) existing.setHighlights(request.getHighlights());
        if (request.getTags()        != null) existing.setTags(request.getTags());
        if (request.getIcon()        != null) existing.setIcon(request.getIcon());
        if (request.getAccent()      != null) existing.setAccent(request.getAccent());
        if (request.getRepoUrl()     != null) existing.setRepoUrl(request.getRepoUrl());
        if (request.getDemoUrl()     != null) existing.setDemoUrl(request.getDemoUrl());
        if (request.getFeatured()    != null) existing.setFeatured(request.getFeatured());
        if (request.getSortOrder()   != null) existing.setSortOrder(request.getSortOrder());

        return projectRepository.save(existing);
    }
}
