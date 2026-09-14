package com.renzmapa.resume_api.experience;

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
@RequestMapping("/experience")
@Tag(name = "Experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    /**
     * GET /api/experience — returns every experience entry, sorted for display.
     * This is the primary endpoint used by the portfolio timeline.
     *
     * @return list of all experience entries, ordered by sortOrder ascending
     */
    @Operation(
        summary = "List all experience ⭐",
        description = "Returns every work experience entry, already sorted by `sortOrder` "
            + "(lowest first) so the frontend can render it straight into the timeline. "
            + "Start here to see what's currently live."
    )
    @ApiResponse(responseCode = "200", description = "Array of experience entries (may be empty)")
    @GetMapping
    public ResponseEntity<List<Experience>> getAll() {
        return ResponseEntity.ok(experienceService.getAll());
    }

    /**
     * GET /api/experience/{id} — returns a single experience entry by ID.
     *
     * @param id the experience ID
     * @return the experience entry, or 404 if not found
     */
    @Operation(
        summary = "Get experience by ID",
        description = "Fetch one specific work experience entry by its database ID."
    )
    @ApiResponse(responseCode = "200", description = "The requested experience entry")
    @ApiResponse(responseCode = "404", description = "Experience with that ID does not exist")
    @GetMapping("/{id}")
    public ResponseEntity<Experience> getById(
            @Parameter(description = "Experience ID (e.g. 1)", example = "1")
            @PathVariable Long id) {
        Experience experience = experienceService.getById(id);
        return experience == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(experience);
    }

    /**
     * POST /api/experience — creates a new experience entry.
     *
     * @param request DTO with the experience fields to set
     * @return the newly created experience entry with HTTP 201 Created
     */
    @Operation(
        summary = "Create new experience",
        description = "Adds a new job to the timeline. Set `sortOrder` to control where it "
            + "appears — lower numbers show up first. Set `current` to `true` if this is "
            + "the role you're in right now."
    )
    @ApiResponse(responseCode = "201", description = "Experience created successfully")
    @PostMapping
    public ResponseEntity<Experience> create(@RequestBody ExperienceRequest request) {
        Experience created = experienceService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PATCH /api/experience/{id} — partial update of an experience entry.
     * Send only the fields you want to change; omitted fields keep their current value.
     *
     * @param id      the experience ID to update
     * @param request DTO containing the fields to change (any subset is valid)
     * @return the updated experience entry, or 404 if the ID was not found
     */
    @Operation(
        summary = "Patch experience by ID",
        description = "Partially updates one experience entry. "
            + "Send only the fields you want to change — everything else stays the same.\n\n"
            + "**Example:** to mark a role as no longer current, send:\n"
            + "```json\n{ \"current\": false }\n```\n\n"
            + "Sending `highlights` or `tags` **replaces** the whole list, so include "
            + "every bullet you want to keep."
    )
    @ApiResponse(responseCode = "200", description = "Experience updated successfully")
    @ApiResponse(responseCode = "404", description = "Experience with that ID does not exist")
    @PatchMapping("/{id}")
    public ResponseEntity<Experience> patch(
            @Parameter(description = "Experience ID to update", example = "1")
            @PathVariable Long id,
            @RequestBody ExperienceRequest request) {

        Experience updated = experienceService.patch(id, request);
        return updated == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/experience/{id} — permanently removes an experience entry.
     *
     * @param id the experience ID to delete
     * @return 204 No Content if deleted, or 404 if the ID was not found
     */
    @Operation(
        summary = "Delete experience by ID",
        description = "Permanently removes one experience entry along with its highlights "
            + "and tags. **This cannot be undone** — there is no version history here, "
            + "so make sure you have the right ID first."
    )
    @ApiResponse(responseCode = "204", description = "Experience deleted successfully (no body returned)")
    @ApiResponse(responseCode = "404", description = "Experience with that ID does not exist")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Experience ID to delete", example = "1")
            @PathVariable Long id) {
        return experienceService.delete(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}
