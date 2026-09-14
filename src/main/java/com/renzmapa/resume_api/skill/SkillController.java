package com.renzmapa.resume_api.skill;

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
@RequestMapping("/skills")
@Tag(name = "Skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * GET /api/skills — returns every skill as a flat list in display order.
     *
     * @return list of all skills, lowest sortOrder first
     */
    @Operation(
        summary = "List all skills (flat)",
        description = "Returns every skill as one flat array, ordered by `sortOrder`. "
            + "Each row carries its own `category`. Use this when you need the IDs — "
            + "for example to patch or delete a single skill."
    )
    @ApiResponse(responseCode = "200", description = "Array of skills (may be empty)")
    @GetMapping
    public ResponseEntity<List<Skill>> getAll() {
        return ResponseEntity.ok(skillService.getAll());
    }

    /**
     * GET /api/skills/grouped — returns skills bundled into their categories.
     * This is the primary endpoint used by the portfolio frontend.
     *
     * @return list of categories, each with its skill names in display order
     */
    @Operation(
        summary = "List skills grouped by category ⭐",
        description = "Returns skills bundled per category, ready to render:\n\n"
            + "```json\n[ { \"category\": \"Programming Languages\", \"items\": [\"Java\", \"Python\"] } ]\n```\n\n"
            + "This is the endpoint the portfolio page uses to build the skills section. "
            + "Category order follows the lowest `sortOrder` in each group, so reordering "
            + "the columns is just a matter of patching a `sortOrder`."
    )
    @ApiResponse(responseCode = "200", description = "Array of skill categories (may be empty)")
    @GetMapping("/grouped")
    public ResponseEntity<List<SkillGroupResponse>> getGrouped() {
        return ResponseEntity.ok(skillService.getGrouped());
    }

    /**
     * GET /api/skills/{id} — returns a single skill by ID.
     *
     * @param id the skill ID
     * @return the skill, or 404 if not found
     */
    @Operation(
        summary = "Get skill by ID",
        description = "Fetch a specific skill by its database ID."
    )
    @ApiResponse(responseCode = "200", description = "The requested skill")
    @ApiResponse(responseCode = "404", description = "Skill with that ID does not exist")
    @GetMapping("/{id}")
    public ResponseEntity<Skill> getById(
            @Parameter(description = "Skill ID (e.g. 1)", example = "1")
            @PathVariable Long id) {
        Skill skill = skillService.getById(id);
        return skill == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(skill);
    }

    /**
     * POST /api/skills — creates a new skill.
     *
     * @param request DTO with the skill fields to set
     * @return the newly created skill with HTTP 201 Created
     */
    @Operation(
        summary = "Create new skill",
        description = "Adds a skill. Reuse an existing `category` string **exactly** to drop it "
            + "into that group, or type a new one to start a fresh group. "
            + "`sortOrder` decides both where the skill sits and where its category sits."
    )
    @ApiResponse(responseCode = "201", description = "Skill created successfully")
    @PostMapping
    public ResponseEntity<Skill> create(@RequestBody SkillRequest request) {
        Skill created = skillService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PATCH /api/skills/{id} — partial update of a skill.
     * Send only the fields you want to change; omitted fields keep their current value.
     *
     * @param id      the skill ID to update
     * @param request DTO containing the fields to change (any subset is valid)
     * @return the updated skill, or 404 if the ID was not found
     */
    @Operation(
        summary = "Patch skill by ID",
        description = "Partially updates a skill. Send only the fields you want to change — "
            + "everything else stays the same.\n\n"
            + "**Example:** to move a skill into another group, send:\n"
            + "```json\n{ \"category\": \"Tools & Platforms\" }\n```"
    )
    @ApiResponse(responseCode = "200", description = "Skill updated successfully")
    @ApiResponse(responseCode = "404", description = "Skill with that ID does not exist")
    @PatchMapping("/{id}")
    public ResponseEntity<Skill> patch(
            @Parameter(description = "Skill ID to update", example = "1")
            @PathVariable Long id,
            @RequestBody SkillRequest request) {

        Skill updated = skillService.patch(id, request);
        return updated == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/skills/{id} — permanently removes a skill.
     *
     * @param id the skill ID to remove
     * @return 204 No Content on success, or 404 if the ID was not found
     */
    @Operation(
        summary = "Delete skill by ID",
        description = "Permanently removes a skill. There is no undo. "
            + "Deleting the last skill in a category also removes that category "
            + "from the grouped response."
    )
    @ApiResponse(responseCode = "204", description = "Skill deleted — nothing is returned")
    @ApiResponse(responseCode = "404", description = "Skill with that ID does not exist")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Skill ID to delete", example = "1")
            @PathVariable Long id) {

        return skillService.delete(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}
