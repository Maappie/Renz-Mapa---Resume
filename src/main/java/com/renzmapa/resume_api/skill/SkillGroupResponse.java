package com.renzmapa.resume_api.skill;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * SkillGroupResponse
 *
 * Response DTO for one category of skills — the shape the portfolio frontend
 * renders as a single skills column.
 *
 * <p>This exists so the API contract does not leak the flat {@code Skill} table.
 * The database stores one row per skill; the frontend wants them bundled per
 * category, and this record is where those two shapes meet. Being a record, it
 * is immutable — the service builds it and nothing downstream can alter it.
 *
 * @param category The group heading, e.g. "Programming Languages".
 * @param items    Skill names in that category, in sortOrder.
 */
@Schema(description = "A category of skills with its member skill names, in display order.")
public record SkillGroupResponse(

    @Schema(description = "Group heading", example = "Programming Languages")
    String category,

    @Schema(description = "Skill names in this category", example = "[\"Java\", \"Python\"]")
    List<String> items
) {}
