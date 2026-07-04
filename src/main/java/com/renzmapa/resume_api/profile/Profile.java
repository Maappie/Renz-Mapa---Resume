package com.renzmapa.resume_api.profile;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String title;
    private String email;
    private String phone;
    private String location;
    private String bio;

    // Stores the list as a comma-separated string in a single column
    @ElementCollection
    @CollectionTable(name = "profile_social_links", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "link")
    private List<String> socialLinks;

    // Required by JPA
    protected Profile() {}

    public Profile(String name, String title, String email, String phone,
                   String location, String bio, List<String> socialLinks) {
        this.name = name;
        this.title = title;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.bio = bio;
        this.socialLinks = socialLinks;
    }

    // Getters
    public Long getId()                  { return id; }
    public String getName()              { return name; }
    public String getTitle()             { return title; }
    public String getEmail()             { return email; }
    public String getPhone()             { return phone; }
    public String getLocation()          { return location; }
    public String getBio()               { return bio; }
    public List<String> getSocialLinks() { return socialLinks; }

    // Setters — used by the patch service to apply partial updates
    public void setName(String name)                     { this.name = name; }
    public void setTitle(String title)                   { this.title = title; }
    public void setEmail(String email)                   { this.email = email; }
    public void setPhone(String phone)                   { this.phone = phone; }
    public void setLocation(String location)             { this.location = location; }
    public void setBio(String bio)                       { this.bio = bio; }
    public void setSocialLinks(List<String> socialLinks) { this.socialLinks = socialLinks; }
}
