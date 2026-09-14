package com.renzmapa.resume_api.skill;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Skill
 *
 * One skill on the portfolio — a single chip such as "Java" or "Spring Boot".
 * Skills are stored flat, one row each, and grouped into their categories at
 * read time by the service. Storing them flat keeps reordering and renaming a
 * one-row update instead of a schema change.
 */
@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Heading this skill is listed under, e.g. "Programming Languages".
     * Skills sharing a category string are grouped together in the response.
     */
    private String category;

    private String name;

    /**
     * Display order across the whole list, lowest first.
     * Grouping preserves it, so a category's position follows the lowest
     * sortOrder of the skills inside it.
     */
    private Integer sortOrder;

    // Required by JPA
    protected Skill() {}

    public Skill(String category, String name, Integer sortOrder) {
        this.category = category;
        this.name = name;
        this.sortOrder = sortOrder;
    }

    // Getters
    public Long getId()                  { return id; }
    public String getCategory()          { return category; }
    public String getName()              { return name; }
    public Integer getSortOrder()        { return sortOrder; }

    // Setters — used by the patch service to apply partial updates
    public void setCategory(String category)                 { this.category = category; }
    public void setName(String name)                         { this.name = name; }
    public void setSortOrder(Integer sortOrder)              { this.sortOrder = sortOrder; }
}
