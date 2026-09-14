package com.renzmapa.resume_api.education;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String school;
    private String degree;
    private String honors;
    private String period;

    @Column(length = 1000)
    private String details;

    private Integer sortOrder;

    // Required by JPA
    protected Education() {}

    public Education(String school, String degree, String honors, String period,
                     String details, Integer sortOrder) {
        this.school = school;
        this.degree = degree;
        this.honors = honors;
        this.period = period;
        this.details = details;
        this.sortOrder = sortOrder;
    }

    // Getters
    public Long getId()                  { return id; }
    public String getSchool()            { return school; }
    public String getDegree()            { return degree; }
    public String getHonors()            { return honors; }
    public String getPeriod()            { return period; }
    public String getDetails()           { return details; }
    public Integer getSortOrder()        { return sortOrder; }

    // Setters — used by the patch service to apply partial updates
    public void setSchool(String school)                 { this.school = school; }
    public void setDegree(String degree)                 { this.degree = degree; }
    public void setHonors(String honors)                 { this.honors = honors; }
    public void setPeriod(String period)                 { this.period = period; }
    public void setDetails(String details)               { this.details = details; }
    public void setSortOrder(Integer sortOrder)          { this.sortOrder = sortOrder; }
}
