package com.renzmapa.resume_api.experience;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * ExperienceRequest
 *
 * DTO for creating or partially updating a work experience entry.
 * Every field is nullable — only non-null fields will overwrite the stored value.
 * You can send any subset of fields; the rest remain untouched.
 *
 * Note that {@code current} and {@code sortOrder} use the boxed types
 * {@code Boolean} / {@code Integer} on purpose: {@code null} means
 * "keep the existing value", not {@code false} / {@code 0}.
 */
@Schema(
    description = "Request body for creating or updating a work experience entry. "
        + "All fields are optional — send only what you want to change."
)
public class ExperienceRequest {

    @Schema(description = "Company or organisation name", example = "Content Lab", nullable = true)
    private String company;

    @Schema(description = "Job title held at the company", example = "Junior Operations Developer", nullable = true)
    private String role;

    @Schema(description = "Human-readable date range", example = "June 2026 — Present", nullable = true)
    private String period;

    @Schema(
        description = "Whether this is your current role. Null keeps the stored value.",
        example = "true",
        nullable = true
    )
    private Boolean current;

    @Schema(
        description = "One or two sentence overview of the role",
        example = "Automating the day-to-day — internal tools, trackers, and integrations.",
        nullable = true
    )
    private String summary;

    @Schema(
        description = "Bullet points describing responsibilities and achievements",
        example = "[\"Built internal tooling\", \"Automated reporting workflows\"]",
        nullable = true
    )
    private List<String> highlights;

    @Schema(
        description = "Technologies and platforms used in this role",
        example = "[\"Google Apps Script\", \"Monday.com\"]",
        nullable = true
    )
    private List<String> tags;

    @Schema(
        description = "Display order — lower numbers appear first. Null keeps the stored value.",
        example = "1",
        nullable = true
    )
    private Integer sortOrder;

    // ─── Getters ─────────────────────────────────────────────
    public String getCompany()               { return company; }
    public String getRole()                  { return role; }
    public String getPeriod()                { return period; }
    public Boolean getCurrent()              { return current; }
    public String getSummary()               { return summary; }
    public List<String> getHighlights()      { return highlights; }
    public List<String> getTags()            { return tags; }
    public Integer getSortOrder()            { return sortOrder; }

    // ─── Setters (required for JSON deserialization) ──────────
    public void setCompany(String company)                   { this.company = company; }
    public void setRole(String role)                         { this.role = role; }
    public void setPeriod(String period)                     { this.period = period; }
    public void setCurrent(Boolean current)                  { this.current = current; }
    public void setSummary(String summary)                   { this.summary = summary; }
    public void setHighlights(List<String> highlights)       { this.highlights = highlights; }
    public void setTags(List<String> tags)                   { this.tags = tags; }
    public void setSortOrder(Integer sortOrder)              { this.sortOrder = sortOrder; }
}
