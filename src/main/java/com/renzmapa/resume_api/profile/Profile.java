package com.renzmapa.resume_api.profile;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

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
    
    private String github;
    private String linkedin;
    private String facebook;

    @ElementCollection
    @CollectionTable(name = "profile_stack", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "tech")
    private List<String> stack = new ArrayList<>();

    // Required by JPA
    protected Profile() {}

    public Profile(String name, String title, String email, String phone,
                   String location, String bio, String github, String linkedin, String facebook,
                   List<String> stack) {
        this.name = name;
        this.title = title;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.bio = bio;
        this.github = github;
        this.linkedin = linkedin;
        this.facebook = facebook;
        this.stack = stack != null ? stack : new ArrayList<>();
    }

    // Getters
    public Long getId()                  { return id; }
    public String getName()              { return name; }
    public String getTitle()             { return title; }
    public String getEmail()             { return email; }
    public String getPhone()             { return phone; }
    public String getLocation()          { return location; }
    public String getBio()               { return bio; }
    public String getGithub()            { return github; }
    public String getLinkedin()          { return linkedin; }
    public String getFacebook()          { return facebook; }
    public List<String> getStack()       { return stack; }

    // Setters — used by the patch service to apply partial updates
    public void setName(String name)                     { this.name = name; }
    public void setTitle(String title)                   { this.title = title; }
    public void setEmail(String email)                   { this.email = email; }
    public void setPhone(String phone)                   { this.phone = phone; }
    public void setLocation(String location)             { this.location = location; }
    public void setBio(String bio)                       { this.bio = bio; }
    public void setGithub(String github)                 { this.github = github; }
    public void setLinkedin(String linkedin)             { this.linkedin = linkedin; }
    public void setFacebook(String facebook)             { this.facebook = facebook; }
    public void setStack(List<String> stack)             { this.stack = stack; }
}
