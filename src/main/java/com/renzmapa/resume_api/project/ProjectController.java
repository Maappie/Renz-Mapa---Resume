package com.renzmapa.resume_api.project;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * GET /api/projects — returns all projects in display order.
     * This is the endpoint the portfolio frontend uses to render the project cards.
     *
     * @return list of all projects, lowest sortOrder first
     */
    @Operation(
        summary = "List all projects ⭐",
        description = "Returns every project, ordered by `sortOrder` (lowest first). "
            + "This is the endpoint the portfolio page uses to build the project cards, "
            + "so the order you see here is the order visitors see."
    )
    @ApiResponse(responseCode = "200", description = "Array of projects (may be empty)")
    @GetMapping
    public ResponseEntity<List<Project>> getAll() {
        return ResponseEntity.ok(projectService.getAll());
    }

    /**
     * GET /api/projects/{id} — returns a single project by ID.
     *
     * @param id the project ID
     * @return the project, or 404 if not found
     */
    @Operation(
        summary = "Get project by ID",
        description = "Fetch a specific project by its database ID."
    )
    @ApiResponse(responseCode = "200", description = "The requested project")
    @ApiResponse(responseCode = "404", description = "Project with that ID does not exist")
    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(
            @Parameter(description = "Project ID (e.g. 1)", example = "1")
            @PathVariable Long id) {
        Project project = projectService.getById(id);
        return project == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(project);
    }

    /**
     * POST /api/projects — creates a new project.
     * Set `sortOrder` to control where the new card lands on the page.
     *
     * @param request DTO with the project fields to set
     * @return the newly created project with HTTP 201 Created
     */
    @Operation(
        summary = "Create new project",
        description = "Adds a project to the portfolio. Give it a `sortOrder` to decide "
            + "where the card appears — lower numbers sit closer to the top. "
            + "`accent` accepts violet, cyan, lime or pink; `icon` takes any Lucide icon name."
    )
    @ApiResponse(responseCode = "201", description = "Project created successfully")
    @PostMapping
    public ResponseEntity<Project> create(@RequestBody ProjectRequest request) {
        Project created = projectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PATCH /api/projects/{id} — partial update of a project.
     * Send only the fields you want to change; omitted fields keep their current value.
     *
     * @param id      the project ID to update
     * @param request DTO containing the fields to change (any subset is valid)
     * @return the updated project, or 404 if the ID was not found
     */
    @Operation(
        summary = "Patch project by ID",
        description = "Partially updates a project. Send only the fields you want to change — "
            + "everything else stays the same.\n\n"
            + "**Example:** to feature a project and move it to the top, send:\n"
            + "```json\n{ \"featured\": true, \"sortOrder\": 1 }\n```"
    )
    @ApiResponse(responseCode = "200", description = "Project updated successfully")
    @ApiResponse(responseCode = "404", description = "Project with that ID does not exist")
    @PatchMapping("/{id}")
    public ResponseEntity<Project> patch(
            @Parameter(description = "Project ID to update", example = "1")
            @PathVariable Long id,
            @RequestBody ProjectRequest request) {

        Project updated = projectService.patch(id, request);
        return updated == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/projects/{id} — permanently removes a project.
     *
     * @param id the project ID to remove
     * @return 204 No Content on success, or 404 if the ID was not found
     */
    @Operation(
        summary = "Delete project by ID",
        description = "Permanently removes a project and its highlights and tags. "
            + "There is no undo — patch `featured` to false if you only want to hide it."
    )
    @ApiResponse(responseCode = "204", description = "Project deleted — nothing is returned")
    @ApiResponse(responseCode = "404", description = "Project with that ID does not exist")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Project ID to delete", example = "1")
            @PathVariable Long id) {

        return projectService.delete(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}
