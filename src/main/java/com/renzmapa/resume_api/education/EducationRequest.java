package com.renzmapa.resume_api.education;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * EducationRequest
 *
 * DTO for creating or partially updating an education entry.
 * Every field is nullable — only non-null fields will overwrite the stored value.
 * You can send any subset of fields; the rest remain untouched.
 *
 * Note that {@code sortOrder} uses the boxed type {@code Integer} on purpose:
 * {@code null} means "keep the existing value", not {@code 0}.
 */
@Schema(
    description = "Request body for creating or updating an education entry. "
        + "All fields are optional — send only what you want to change."
)
public class EducationRequest {

    @Schema(
        description = "School or university name",
        example = "Pamantasan ng Lungsod ng Maynila",
        nullable = true
    )
    private String school;

    @Schema(
        description = "Degree or programme completed",
        example = "Bachelor of Science in Computer Engineering",
        nullable = true
    )
    private String degree;

    @Schema(description = "Latin honours or distinction, if any", example = "Cum Laude", nullable = true)
    private String honors;

    @Schema(description = "Human-readable date range", example = "2022 — 2026", nullable = true)
    private String period;

    @Schema(
        description = "Longer description of coursework, focus areas, or achievements",
        example = "Computer Engineering with a focus on software systems and networking.",
        nullable = true
    )
    private String details;

    @Schema(
        description = "Display order — lower numbers appear first. Null keeps the stored value.",
        example = "1",
        nullable = true
    )
    private Integer sortOrder;

    // ─── Getters ─────────────────────────────────────────────
    public String getSchool()                { return school; }
    public String getDegree()                { return degree; }
    public String getHonors()                { return honors; }
    public String getPeriod()                { return period; }
    public String getDetails()               { return details; }
    public Integer getSortOrder()            { return sortOrder; }

    // ─── Setters (required for JSON deserialization) ──────────
    public void setSchool(String school)                 { this.school = school; }
    public void setDegree(String degree)                 { this.degree = degree; }
    public void setHonors(String honors)                 { this.honors = honors; }
    public void setPeriod(String period)                 { this.period = period; }
    public void setDetails(String details)               { this.details = details; }
    public void setSortOrder(Integer sortOrder)          { this.sortOrder = sortOrder; }
}
