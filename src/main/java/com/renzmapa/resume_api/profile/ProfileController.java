package com.renzmapa.resume_api.profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/profile")
@Tag(name = "Profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * GET /api/profile — returns all profiles.
     *
     * @return list of all profile entries in the database
     */
    @Operation(
        summary = "List all profiles",
        description = "Returns every profile row in the database, ordered by ID. "
            + "Useful for viewing version history."
    )
    @ApiResponse(responseCode = "200", description = "Array of profiles (may be empty)")
    @GetMapping
    public ResponseEntity<List<Profile>> getAll() {
        return ResponseEntity.ok(profileService.getAll());
    }

    /**
     * GET /api/profile/latest — returns the most recently created profile.
     * This is the primary endpoint used by the portfolio frontend.
     *
     * @return the latest profile, or 404 if no profiles exist
     */
    @Operation(
        summary = "Get latest profile ⭐",
        description = "Returns the most recently created profile (highest ID). "
            + "This is the endpoint the portfolio homepage uses to display your data. "
            + "Try this first to see what's currently live."
    )
    @ApiResponse(responseCode = "200", description = "The latest profile")
    @ApiResponse(responseCode = "404", description = "No profiles exist yet — use POST to create one")
    @GetMapping("/latest")
    public ResponseEntity<Profile> getLatest() {
        Profile profile = profileService.getLatest();
        return profile == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(profile);
    }

    /**
     * GET /api/profile/{id} — returns a single profile by ID.
     *
     * @param id the profile ID
     * @return the profile, or 404 if not found
     */
    @Operation(
        summary = "Get profile by ID",
        description = "Fetch a specific profile version by its database ID."
    )
    @ApiResponse(responseCode = "200", description = "The requested profile")
    @ApiResponse(responseCode = "404", description = "Profile with that ID does not exist")
    @GetMapping("/{id}")
    public ResponseEntity<Profile> getById(
            @Parameter(description = "Profile ID (e.g. 1)", example = "1")
            @PathVariable Long id) {
        Profile profile = profileService.getById(id);
        return profile == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(profile);
    }

    /**
     * POST /api/profile — creates a new profile row.
     * This is the "update by adding" approach: old profiles are preserved as history,
     * and the portfolio always displays the latest one.
     *
     * @param request DTO with the profile fields to set
     * @return the newly created profile with HTTP 201 Created
     */
    @Operation(
        summary = "Create new profile",
        description = "Inserts a new profile row. The previous profile is **not** deleted — "
            + "it stays as version history. The portfolio will automatically show this "
            + "new entry because it always reads the latest."
    )
    @ApiResponse(responseCode = "201", description = "Profile created successfully")
    @PostMapping
    public ResponseEntity<Profile> create(@RequestBody ProfileUpdateRequest request) {
        Profile created = profileService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PATCH /api/profile/latest — partial update of the most recent profile.
     * Send only the fields you want to change; omitted fields keep their current value.
     *
     * @param request DTO containing the fields to change (any subset is valid)
     * @return the updated profile, or 404 if no profiles exist
     */
    @Operation(
        summary = "Patch latest profile ⭐",
        description = "Partially updates the most recent profile. **You don't need to know the ID.** "
            + "Send only the fields you want to change — everything else stays the same.\n\n"
            + "**Example:** to change just the title, send:\n"
            + "```json\n{ \"title\": \"Senior Developer\" }\n```"
    )
    @ApiResponse(responseCode = "200", description = "Profile updated successfully")
    @ApiResponse(responseCode = "404", description = "No profiles exist yet — use POST to create one first")
    @PatchMapping("/latest")
    public ResponseEntity<Profile> patchLatest(@RequestBody ProfileUpdateRequest request) {
        Profile updated = profileService.patchLatest(request);
        return updated == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(updated);
    }

    /**
     * PATCH /api/profile/{id} — partial update of a specific profile by ID.
     * Send only the fields you want to change; omitted fields keep their current value.
     *
     * @param id      the profile ID to update
     * @param request DTO containing the fields to change (any subset is valid)
     * @return the updated profile, or 404 if the ID was not found
     */
    @Operation(
        summary = "Patch profile by ID",
        description = "Partially updates a specific profile version. "
            + "Send only the fields you want to change."
    )
    @ApiResponse(responseCode = "200", description = "Profile updated successfully")
    @ApiResponse(responseCode = "404", description = "Profile with that ID does not exist")
    @PatchMapping("/{id}")
    public ResponseEntity<Profile> patch(
            @Parameter(description = "Profile ID to update", example = "1")
            @PathVariable Long id,
            @RequestBody ProfileUpdateRequest request) {

        Profile updated = profileService.patch(id, request);
        return updated == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(updated);
    }
}
