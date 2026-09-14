package com.renzmapa.resume_api.education;

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
@RequestMapping("/education")
@Tag(name = "Education")
public class EducationController {

    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    /**
     * GET /api/education — returns every education entry, sorted for display.
     * This is the primary endpoint used by the portfolio education section.
     *
     * @return list of all education entries, ordered by sortOrder ascending
     */
    @Operation(
        summary = "List all education ⭐",
        description = "Returns every education entry, already sorted by `sortOrder` "
            + "(lowest first) so the frontend can render it as-is. "
            + "Start here to see what's currently live."
    )
    @ApiResponse(responseCode = "200", description = "Array of education entries (may be empty)")
    @GetMapping
    public ResponseEntity<List<Education>> getAll() {
        return ResponseEntity.ok(educationService.getAll());
    }

    /**
     * GET /api/education/{id} — returns a single education entry by ID.
     *
     * @param id the education ID
     * @return the education entry, or 404 if not found
     */
    @Operation(
        summary = "Get education by ID",
        description = "Fetch one specific education entry by its database ID."
    )
    @ApiResponse(responseCode = "200", description = "The requested education entry")
    @ApiResponse(responseCode = "404", description = "Education with that ID does not exist")
    @GetMapping("/{id}")
    public ResponseEntity<Education> getById(
            @Parameter(description = "Education ID (e.g. 1)", example = "1")
            @PathVariable Long id) {
        Education education = educationService.getById(id);
        return education == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(education);
    }

    /**
     * POST /api/education — creates a new education entry.
     *
     * @param request DTO with the education fields to set
     * @return the newly created education entry with HTTP 201 Created
     */
    @Operation(
        summary = "Create new education",
        description = "Adds a school, degree, or certification to the education section. "
            + "Set `sortOrder` to control where it appears — lower numbers show up first. "
            + "Leave `honors` empty if there isn't one."
    )
    @ApiResponse(responseCode = "201", description = "Education created successfully")
    @PostMapping
    public ResponseEntity<Education> create(@RequestBody EducationRequest request) {
        Education created = educationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PATCH /api/education/{id} — partial update of an education entry.
     * Send only the fields you want to change; omitted fields keep their current value.
     *
     * @param id      the education ID to update
     * @param request DTO containing the fields to change (any subset is valid)
     * @return the updated education entry, or 404 if the ID was not found
     */
    @Operation(
        summary = "Patch education by ID",
        description = "Partially updates one education entry. "
            + "Send only the fields you want to change — everything else stays the same.\n\n"
            + "**Example:** to fix just the honours line, send:\n"
            + "```json\n{ \"honors\": \"Magna Cum Laude\" }\n```"
    )
    @ApiResponse(responseCode = "200", description = "Education updated successfully")
    @ApiResponse(responseCode = "404", description = "Education with that ID does not exist")
    @PatchMapping("/{id}")
    public ResponseEntity<Education> patch(
            @Parameter(description = "Education ID to update", example = "1")
            @PathVariable Long id,
            @RequestBody EducationRequest request) {

        Education updated = educationService.patch(id, request);
        return updated == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/education/{id} — permanently removes an education entry.
     *
     * @param id the education ID to delete
     * @return 204 No Content if deleted, or 404 if the ID was not found
     */
    @Operation(
        summary = "Delete education by ID",
        description = "Permanently removes one education entry. "
            + "**This cannot be undone** — there is no version history here, "
            + "so make sure you have the right ID first."
    )
    @ApiResponse(responseCode = "204", description = "Education deleted successfully (no body returned)")
    @ApiResponse(responseCode = "404", description = "Education with that ID does not exist")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Education ID to delete", example = "1")
            @PathVariable Long id) {
        return educationService.delete(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}
