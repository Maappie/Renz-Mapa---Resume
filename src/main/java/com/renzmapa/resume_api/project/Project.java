package com.renzmapa.resume_api.project;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Project
 *
 * A single portfolio project entry — one case-study card on the portfolio page.
 * Rows are rendered in ascending {@code sortOrder}, so the display order is data,
 * not code.
 */
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String role;
    private String year;

    @Column(length = 1000)
    private String blurb;

    @ElementCollection
    @CollectionTable(name = "project_highlights", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "highlight")
    private List<String> highlights = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_tags", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    /**
     * Name of a Lucide icon rendered on the project card (e.g. "sprout", "terminal", "church").
     * The frontend feeds this string straight to Lucide, so it must match a real icon id —
     * an unknown name renders nothing rather than failing loudly.
     */
    private String icon;

    /**
     * Frontend colour key for the card's accent treatment.
     * One of: "violet" | "cyan" | "lime" | "pink".
     * These are keys, not hex values — the frontend maps them onto CSS custom properties
     * in global.css, so the palette can be re-themed without touching the database.
     */
    private String accent;

    private String repoUrl;
    private String demoUrl;

    private boolean featured;
    private Integer sortOrder;

    // Required by JPA
    protected Project() {}

    public Project(String name, String role, String year, String blurb,
                   List<String> highlights, List<String> tags, String icon, String accent,
                   String repoUrl, String demoUrl, boolean featured, Integer sortOrder) {
        this.name = name;
        this.role = role;
        this.year = year;
        this.blurb = blurb;
        // Copied, not aliased — callers may hand us an immutable List.of(...) and
        // Hibernate needs a collection it is allowed to mutate.
        this.highlights = highlights != null ? new ArrayList<>(highlights) : new ArrayList<>();
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        this.icon = icon;
        this.accent = accent;
        this.repoUrl = repoUrl;
        this.demoUrl = demoUrl;
        this.featured = featured;
        this.sortOrder = sortOrder;
    }

    // Getters
    public Long getId()                  { return id; }
    public String getName()              { return name; }
    public String getRole()              { return role; }
    public String getYear()              { return year; }
    public String getBlurb()             { return blurb; }
    public List<String> getHighlights()  { return highlights; }
    public List<String> getTags()        { return tags; }
    public String getIcon()              { return icon; }
    public String getAccent()            { return accent; }
    public String getRepoUrl()           { return repoUrl; }
    public String getDemoUrl()           { return demoUrl; }
    public boolean isFeatured()          { return featured; }
    public Integer getSortOrder()        { return sortOrder; }

    // Setters — used by the patch service to apply partial updates
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
    public void setFeatured(boolean featured)                { this.featured = featured; }
    public void setSortOrder(Integer sortOrder)              { this.sortOrder = sortOrder; }
}
