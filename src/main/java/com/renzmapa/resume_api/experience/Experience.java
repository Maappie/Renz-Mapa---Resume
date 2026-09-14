package com.renzmapa.resume_api.experience;

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

@Entity
@Table(name = "experiences")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;
    private String role;
    private String period;
    private boolean current;

    @Column(length = 1000)
    private String summary;

    @ElementCollection
    @CollectionTable(name = "experience_highlights", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "highlight", length = 1000)
    private List<String> highlights = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "experience_tags", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    private Integer sortOrder;

    // Required by JPA
    protected Experience() {}

    public Experience(String company, String role, String period, boolean current,
                      String summary, List<String> highlights, List<String> tags,
                      Integer sortOrder) {
        this.company = company;
        this.role = role;
        this.period = period;
        this.current = current;
        this.summary = summary;
        this.highlights = highlights != null ? highlights : new ArrayList<>();
        this.tags = tags != null ? tags : new ArrayList<>();
        this.sortOrder = sortOrder;
    }

    // Getters
    public Long getId()                  { return id; }
    public String getCompany()           { return company; }
    public String getRole()              { return role; }
    public String getPeriod()            { return period; }
    public boolean isCurrent()           { return current; }
    public String getSummary()           { return summary; }
    public List<String> getHighlights()  { return highlights; }
    public List<String> getTags()        { return tags; }
    public Integer getSortOrder()        { return sortOrder; }

    // Setters — used by the patch service to apply partial updates
    public void setCompany(String company)                   { this.company = company; }
    public void setRole(String role)                         { this.role = role; }
    public void setPeriod(String period)                     { this.period = period; }
    public void setCurrent(boolean current)                  { this.current = current; }
    public void setSummary(String summary)                   { this.summary = summary; }
    public void setHighlights(List<String> highlights)       { this.highlights = highlights; }
    public void setTags(List<String> tags)                   { this.tags = tags; }
    public void setSortOrder(Integer sortOrder)              { this.sortOrder = sortOrder; }
}
