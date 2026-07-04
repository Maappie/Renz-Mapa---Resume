# Profile Settings Page Guide

This guide walks you through building the `/profile/settings` page — a plain HTML form that reads from and writes to the SQLite database via the REST API.

---

## Overview

### What you will build

A settings page at `http://localhost:8080/profile/settings` with:
- Input fields pre-filled with current profile data loaded from the database
- A submit button that saves changes via the REST API

### Why two controllers?

You need **two separate controllers** for the profile feature:

| Controller | Annotation | URL prefix | Returns |
|---|---|---|---|
| `ProfileController` | `@RestController` | `/api/profile` (auto-prefixed) | JSON |
| `ProfileSettingsController` | `@Controller` | `/profile/settings` (no auto-prefix) | HTML page |

`@RestController` = `@Controller` + `@ResponseBody`. Every method in it writes data (JSON) directly into the HTTP response. You **cannot** serve an HTML page from it.

`@Controller` without `@ResponseBody` can forward/redirect requests to static files — which is what you need to serve the settings HTML page.

> [!IMPORTANT]
> `WebConfig.java` auto-prefixes `/api` to **all `@RestController` classes only**. Your new `@Controller` will NOT get that prefix, so `/profile/settings` is exactly the URL it will serve.

---

## File Changes Summary

```
profile/
├── Profile.java                        (no change)
├── ProfileRepository.java              (no change)
├── ProfileService.java                 ← Add save() and update()
├── ProfileController.java              ← Add POST and PUT endpoints
└── ProfileSettingsController.java      ← NEW: serves the HTML page

static/
├── profile/
│   └── settings.html                   ← NEW: plain HTML form
└── js/
    ├── api/
    │   └── profileApi.js               ← Add saveProfile() and updateProfile()
    └── components/
        └── settingsForm.js             ← NEW: populates and submits the form
```

---

## Step 1 — Add `save` and `update` to `ProfileService`

Open [ProfileService.java](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/java/com/renzmapa/resume_api/profile/ProfileService.java) and add two methods below `getById`:

```java
/**
 * Saves a new profile to the database.
 *
 * @param profile the new profile to persist
 * @return the saved profile with its generated ID
 */
public Profile save(Profile profile) {
    return profileRepository.save(profile);
}

/**
 * Updates an existing profile by ID.
 *
 * <p>Finds the existing record, applies the new values, and persists.
 * Returns null if the profile with the given ID does not exist.
 *
 * @param id      the ID of the profile to update
 * @param updated a Profile object carrying the new field values
 * @return the updated profile, or null if not found
 */
public Profile update(Long id, Profile updated) {
    Profile existing = profileRepository.findById(id).orElse(null);

    if (existing == null) {
        return null;
    }

    existing.setName(updated.getName());
    existing.setTitle(updated.getTitle());
    existing.setEmail(updated.getEmail());
    existing.setPhone(updated.getPhone());
    existing.setLocation(updated.getLocation());
    existing.setBio(updated.getBio());
    existing.setSocialLinks(updated.getSocialLinks());

    return profileRepository.save(existing);
}
```

> [!NOTE]
> The `update` method calls setters (`setName`, `setTitle`, etc.) on the existing entity. You will need to add those setters to `Profile.java` — see Step 2.

---

## Step 2 — Add Setters to `Profile.java`

Open [Profile.java](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/java/com/renzmapa/resume_api/profile/Profile.java) and add setters below the existing getters:

```java
// Setters (required for JPA update operations)
public void setName(String name)                    { this.name = name; }
public void setTitle(String title)                  { this.title = title; }
public void setEmail(String email)                  { this.email = email; }
public void setPhone(String phone)                  { this.phone = phone; }
public void setLocation(String location)            { this.location = location; }
public void setBio(String bio)                      { this.bio = bio; }
public void setSocialLinks(List<String> socialLinks){ this.socialLinks = socialLinks; }
```

---

## Step 3 — Add `POST` and `PUT` Endpoints to `ProfileController`

Open [ProfileController.java](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/java/com/renzmapa/resume_api/profile/ProfileController.java) and add two methods below `getById`:

```java
/**
 * POST /api/profile
 *
 * Creates a new profile record in the database.
 * Returns 201 Created with the saved profile (including generated ID).
 */
@PostMapping
public ResponseEntity<Profile> create(@RequestBody Profile profile) {
    Profile saved = profileService.save(profile);
    return ResponseEntity.status(201).body(saved);
}

/**
 * PUT /api/profile/{id}
 *
 * Updates an existing profile by ID.
 * Returns 200 OK with the updated profile, or 404 if not found.
 */
@PutMapping("/{id}")
public ResponseEntity<Profile> update(@PathVariable Long id, @RequestBody Profile profile) {
    Profile updated = profileService.update(id, profile);
    return updated == null
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(updated);
}
```

> [!TIP]
> `@RequestBody` tells Spring to parse the incoming JSON body and map it to a `Profile` object automatically using Jackson.

---

## Step 4 — Create `ProfileSettingsController`

Create a **new file** in the `profile/` package:

**File**: `src/main/java/com/renzmapa/resume_api/profile/ProfileSettingsController.java`

```java
package com.renzmapa.resume_api.profile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Serves the Profile Settings HTML page.
 *
 * <p>Uses @Controller (NOT @RestController) so it can forward to a static HTML
 * file. Because it is not a @RestController, WebConfig will NOT prefix it with
 * "/api", meaning it is accessible directly at /profile/settings.
 */
@Controller
@RequestMapping("/profile")
public class ProfileSettingsController {

    /**
     * GET /profile/settings
     *
     * Forwards the request to the static settings HTML page.
     */
    @GetMapping("/settings")
    public String settingsPage() {
        return "forward:/profile/settings.html";
    }
}
```

---

## Step 5 — Create `settings.html`

Create the directory `src/main/resources/static/profile/` and add:

**File**: `src/main/resources/static/profile/settings.html`

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile Settings</title>
</head>
<body>
    <h1>Profile Settings</h1>

    <form id="profile-form">
        <div>
            <label for="name">Name</label>
            <input type="text" id="name" name="name">
        </div>
        <div>
            <label for="title">Title</label>
            <input type="text" id="title" name="title">
        </div>
        <div>
            <label for="email">Email</label>
            <input type="email" id="email" name="email">
        </div>
        <div>
            <label for="phone">Phone</label>
            <input type="tel" id="phone" name="phone">
        </div>
        <div>
            <label for="location">Location</label>
            <input type="text" id="location" name="location">
        </div>
        <div>
            <label for="bio">Bio</label>
            <textarea id="bio" name="bio"></textarea>
        </div>
        <div>
            <label for="social-links">Social Links (one per line)</label>
            <textarea id="social-links" name="socialLinks"></textarea>
        </div>

        <p id="status-message"></p>
        <button type="submit">Save Profile</button>
    </form>

    <script type="module" src="/js/components/settingsForm.js"></script>
</body>
</html>
```

---

## Step 6 — Update `profileApi.js`

Open [profileApi.js](file:///c:/Users/Renz/Personal%20Projects/Renz%20Mapa%20-%20Resume/src/main/resources/static/js/api/profileApi.js) and add two functions:

```js
/**
 * Sends a PUT request to update an existing profile by ID.
 *
 * @param {number} id - The profile ID to update.
 * @param {Object} profileData - The updated profile fields.
 * @returns {Promise<Object>} The updated profile from the server.
 */
export async function updateProfile(id, profileData) {
    const response = await fetch(`/api/profile/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(profileData)
    });

    if (!response.ok) {
        throw new Error(`Failed to update profile: ${response.status} ${response.statusText}`);
    }

    return response.json();
}

/**
 * Sends a POST request to create a new profile.
 *
 * @param {Object} profileData - The new profile fields.
 * @returns {Promise<Object>} The saved profile from the server (includes generated ID).
 */
export async function createProfile(profileData) {
    const response = await fetch('/api/profile', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(profileData)
    });

    if (!response.ok) {
        throw new Error(`Failed to create profile: ${response.status} ${response.statusText}`);
    }

    return response.json();
}
```

---

## Step 7 — Create `settingsForm.js`

Create a new file:

**File**: `src/main/resources/static/js/components/settingsForm.js`

```js
import { fetchProfileData } from '../api/profileApi.js';
import { updateProfile } from '../api/profileApi.js';

const PROFILE_ID = 1; // Target the first profile record

/**
 * Pre-fills all form inputs with existing data from the database.
 */
async function populateForm() {
    try {
        const profile = await fetchProfileData();

        document.getElementById('name').value         = profile.name        || '';
        document.getElementById('title').value        = profile.title       || '';
        document.getElementById('email').value        = profile.email       || '';
        document.getElementById('phone').value        = profile.phone       || '';
        document.getElementById('location').value     = profile.location    || '';
        document.getElementById('bio').value          = profile.bio         || '';
        document.getElementById('social-links').value = (profile.socialLinks || []).join('\n');
    } catch (error) {
        console.error('Failed to load profile data:', error);
        document.getElementById('status-message').textContent = 'Error loading profile data.';
    }
}

/**
 * Reads the form inputs and submits an update to the REST API.
 */
async function handleSubmit(event) {
    event.preventDefault();

    const statusEl = document.getElementById('status-message');
    statusEl.textContent = 'Saving...';

    const socialLinksRaw = document.getElementById('social-links').value;
    const socialLinks = socialLinksRaw
        .split('\n')
        .map(link => link.trim())
        .filter(link => link.length > 0);

    const profileData = {
        name:        document.getElementById('name').value,
        title:       document.getElementById('title').value,
        email:       document.getElementById('email').value,
        phone:       document.getElementById('phone').value,
        location:    document.getElementById('location').value,
        bio:         document.getElementById('bio').value,
        socialLinks: socialLinks
    };

    try {
        await updateProfile(PROFILE_ID, profileData);
        statusEl.textContent = 'Profile saved successfully.';
    } catch (error) {
        console.error('Failed to save profile:', error);
        statusEl.textContent = 'Error saving profile. Please try again.';
    }
}

// Initialise
document.addEventListener('DOMContentLoaded', () => {
    populateForm();
    document.getElementById('profile-form').addEventListener('submit', handleSubmit);
});
```

---

## How it All Connects

```
Browser visits /profile/settings
        │
        ▼
ProfileSettingsController (@Controller)
        │  GET /profile/settings
        │  forwards to →
        ▼
static/profile/settings.html
        │  loads →
        ▼
js/components/settingsForm.js
        │  on DOMContentLoaded →
        │  calls fetchProfileData() → GET /api/profile/1 → pre-fills form
        │
        │  on form submit →
        │  calls updateProfile(1, data) → PUT /api/profile/1
        ▼
ProfileController (@RestController)
        │  PUT /api/profile/{id}
        ▼
ProfileService.update()
        │
        ▼
ProfileRepository.save()   → SQLite database
```

---

## Final File Structure (profile package)

```
profile/
├── Profile.java                     @Entity — model + setters added
├── ProfileRepository.java           JpaRepository — no change
├── ProfileService.java              + save(), update()
├── ProfileController.java           + POST /api/profile, PUT /api/profile/{id}
└── ProfileSettingsController.java   NEW — @Controller, serves /profile/settings

static/
├── profile/
│   └── settings.html                NEW — plain HTML form
└── js/
    ├── api/
    │   └── profileApi.js            + updateProfile(), createProfile()
    └── components/
        └── settingsForm.js          NEW — form population + submit handler
```
