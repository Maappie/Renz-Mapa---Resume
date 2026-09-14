package com.renzmapa.resume_api.skill;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SkillRequest
 *
 * DTO for creating or partially updating a skill.
 * Every field is nullable — only non-null fields will overwrite the stored value.
 * You can send any subset of fields; the rest keep their existing value.
 */
@Schema(
    description = "Request body for creating or updating a skill. "
        + "All fields are optional — send only what you want to change."
)
public class SkillRequest {

    @Schema(
        description = "Heading the skill is grouped under. Reuse an existing category "
            + "string exactly to join that group, or invent a new one to start a group.",
        example = "Programming Languages",
        nullable = true
    )
    private String category;

    @Schema(description = "Skill name as displayed", example = "Java", nullable = true)
    private String name;

    @Schema(
        description = "Display order across the whole list, lowest first. "
            + "Category order follows the lowest sortOrder inside each group.",
        example = "1",
        nullable = true
    )
    private Integer sortOrder;

    // ─── Getters ─────────────────────────────────────────────
    public String getCategory()              { return category; }
    public String getName()                  { return name; }
    public Integer getSortOrder()            { return sortOrder; }

    // ─── Setters (required for JSON deserialization) ──────────
    public void setCategory(String category)                 { this.category = category; }
    public void setName(String name)                         { this.name = name; }
    public void setSortOrder(Integer sortOrder)              { this.sortOrder = sortOrder; }
}
