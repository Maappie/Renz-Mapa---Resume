package com.renzmapa.resume_api.project;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * ProjectRequest
 *
 * DTO for creating or partially updating a project.
 * Every field is nullable — only non-null fields will overwrite the stored value.
 * You can send any subset of fields; the rest keep their existing value.
 *
 * <p>Note that {@code featured} and {@code sortOrder} are boxed here on purpose:
 * a primitive {@code boolean} would silently default to {@code false} and clear
 * the flag on every patch that did not mention it.
 */
@Schema(
    description = "Request body for creating or updating a project. "
        + "All fields are optional — send only what you want to change."
)
public class ProjectRequest {

    @Schema(description = "Project name", example = "LettuVault", nullable = true)
    private String name;

    @Schema(description = "Your role on the project", example = "Lead Systems Architect & Developer", nullable = true)
    private String role;

    @Schema(description = "Year the project was built", example = "2026", nullable = true)
    private String year;

    @Schema(
        description = "Short summary shown on the project card (up to 1000 characters)",
        example = "A modular IoT ecosystem for automated monitoring and control of high-value crop environments.",
        nullable = true
    )
    private String blurb;

    @Schema(
        description = "Bullet points describing what you built",
        example = "[\"Engineered a modular IoT ecosystem using ESP32, MQTT, and FastAPI.\"]",
        nullable = true
    )
    private List<String> highlights;

    @Schema(
        description = "Technology tags shown as chips on the card",
        example = "[\"ESP32\", \"MQTT\", \"FastAPI\"]",
        nullable = true
    )
    private List<String> tags;

    @Schema(description = "Lucide icon name for the card", example = "sprout", nullable = true)
    private String icon;

    @Schema(
        description = "Frontend colour key: violet | cyan | lime | pink",
        example = "lime",
        nullable = true
    )
    private String accent;

    @Schema(description = "Source repository link", example = "https://github.com/renzmapa/lettuvault", nullable = true)
    private String repoUrl;

    @Schema(description = "Live demo link", example = "https://lettuvault.example.com", nullable = true)
    private String demoUrl;

    @Schema(description = "Whether the project is highlighted on the portfolio", example = "true", nullable = true)
    private Boolean featured;

    @Schema(description = "Display order, lowest first", example = "1", nullable = true)
    private Integer sortOrder;

    // ─── Getters ─────────────────────────────────────────────
    public String getName()                  { return name; }
    public String getRole()                  { return role; }
    public String getYear()                  { return year; }
    public String getBlurb()                 { return blurb; }
    public List<String> getHighlights()      { return highlights; }
    public List<String> getTags()            { return tags; }
    public String getIcon()                  { return icon; }
    public String getAccent()                { return accent; }
    public String getRepoUrl()               { return repoUrl; }
    public String getDemoUrl()               { return demoUrl; }
    public Boolean getFeatured()             { return featured; }
    public Integer getSortOrder()            { return sortOrder; }

    // ─── Setters (required for JSON deserialization) ──────────
    public void setName(String name)                         { this.name = name; }
    public void setRole(String role)                         { this.role = role; }
    public void setYear(String year)                         { this.year = year; }
    public void setBlurb(String blurb)                       { this.blurb = blurb; }
    public void setHighlights(List<String> highlights)       { this.highlights = highlights; }
    public void setTags(List<String> tags)                   { this.tags = tags; }
    public void setIcon(String icon)                         { this.icon = icon; }
    public void setAccent(String accent)                     { this.accent = accent; }
    public void setRepoUrl(String repoUrl)                   { this.repoUrl = repoUrl; }
    public void setDemoUrl(String demoUrl)                   { this.demoUrl = demoUrl; }
    public void setFeatured(Boolean featured)                { this.featured = featured; }
    public void setSortOrder(Integer sortOrder)              { this.sortOrder = sortOrder; }
}
