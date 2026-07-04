package com.renzmapa.resume_api.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * ProfileUpdateRequest
 *
 * DTO for creating or partially updating a profile.
 * Every field is nullable — only non-null fields will overwrite the stored value.
 * You can send any subset of fields; the rest remain untouched.
 */
@Schema(
    description = "Request body for creating or updating a profile. "
        + "All fields are optional — send only what you want to change."
)
public class ProfileUpdateRequest {

    @Schema(description = "Full display name", example = "Renz Mapa", nullable = true)
    private String name;

    @Schema(description = "Job title or role", example = "Full Stack Developer", nullable = true)
    private String title;

    @Schema(description = "Contact email", example = "renz@example.com", nullable = true)
    private String email;

    @Schema(description = "Phone number", example = "+63 917 123 4567", nullable = true)
    private String phone;

    @Schema(description = "City or region", example = "Manila, Philippines", nullable = true)
    private String location;

    @Schema(description = "Short biography or intro", example = "I build things for the web.", nullable = true)
    private String bio;

    @Schema(
        description = "List of social media or portfolio URLs",
        example = "[\"https://github.com/renzmapa\", \"https://linkedin.com/in/renzmapa\"]",
        nullable = true
    )
    private List<String> socialLinks;

    // ─── Getters ─────────────────────────────────────────────
    public String getName()                  { return name; }
    public String getTitle()                 { return title; }
    public String getEmail()                 { return email; }
    public String getPhone()                 { return phone; }
    public String getLocation()              { return location; }
    public String getBio()                   { return bio; }
    public List<String> getSocialLinks()     { return socialLinks; }

    // ─── Setters (required for JSON deserialization) ──────────
    public void setName(String name)                     { this.name = name; }
    public void setTitle(String title)                   { this.title = title; }
    public void setEmail(String email)                   { this.email = email; }
    public void setPhone(String phone)                   { this.phone = phone; }
    public void setLocation(String location)             { this.location = location; }
    public void setBio(String bio)                       { this.bio = bio; }
    public void setSocialLinks(List<String> socialLinks) { this.socialLinks = socialLinks; }
}
