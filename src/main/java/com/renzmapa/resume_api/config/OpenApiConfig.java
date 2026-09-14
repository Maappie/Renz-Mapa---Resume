package com.renzmapa.resume_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenApiConfig
 *
 * Configures the OpenAPI (Swagger) metadata and tag groupings for the interactive
 * API docs page at /admin/api-docs.
 *
 * Tags are ordered to surface the most-used endpoints first:
 * 1. Profile — the core feature
 * 2. Experience — work history shown on the portfolio timeline
 * 3. Projects — portfolio project cards
 * 4. Skills — grouped tech stack
 * 5. Education — schools and degrees
 * 6. Features — secondary CRUD
 */
@Configuration
public class OpenApiConfig {

    /**
     * Global API metadata and tag ordering.
     *
     * @return OpenAPI spec with title, description, version, contact, and tag order
     */
    @Bean
    public OpenAPI resumeApiSpec() {
        return new OpenAPI()
            .info(new Info()
                .title("Renz Mapa — Resume API")
                .description(
                    "Interactive API for the portfolio/resume system.\n\n"
                    + "**How to use this page:**\n"
                    + "1. Expand a section below to see its endpoints.\n"
                    + "2. Click **Try it out** on any endpoint.\n"
                    + "3. Fill in the request body (only include fields you want to change).\n"
                    + "4. Click **Execute** to send the request.\n\n"
                    + "**Partial updates:** PATCH endpoints accept any subset of fields. "
                    + "Omitted fields keep their current value. "
                    + "Send `null` to skip a field, or `\"\"` to clear it."
                )
                .version("1.0.0")
                .contact(new Contact()
                    .name("Renz Mapa")
                )
            )
            .tags(List.of(
                new Tag()
                    .name("Profile")
                    .description("Your personal profile — name, title, bio, contact info, and social links. "
                        + "The portfolio homepage reads from `/latest`. "
                        + "Use POST to create a new version, or PATCH to edit the current one."),
                new Tag()
                    .name("Experience")
                    .description("Work history rendered as the portfolio's experience timeline. "
                        + "Entries are returned in `sortOrder` order; set `current` to true to "
                        + "show the pulsing \"Now\" badge."),
                new Tag()
                    .name("Projects")
                    .description("Project cards on the portfolio. Each project carries its own "
                        + "`icon` (a Lucide icon name) and `accent` (violet, cyan, lime or pink), "
                        + "so you can restyle a card without touching the frontend."),
                new Tag()
                    .name("Skills")
                    .description("Your tech stack. The portfolio reads `/grouped`, which returns "
                        + "skills already bundled by category in display order."),
                new Tag()
                    .name("Education")
                    .description("Schools, degrees and honours shown in the education section."),
                new Tag()
                    .name("Features")
                    .description("Feature flags and toggleable capabilities for the portfolio.")
            ));
    }

    /**
     * Groups all /api/profile/** endpoints under the "Profile" section.
     *
     * @return grouped API definition for the profile feature
     */
    @Bean
    public GroupedOpenApi profileGroup() {
        return GroupedOpenApi.builder()
            .group("1-profile")
            .displayName("Profile")
            .pathsToMatch("/api/profile/**", "/api/profile")
            .build();
    }

    /**
     * Groups all /api/experience/** endpoints under the "Experience" section.
     *
     * @return grouped API definition for the experience feature
     */
    @Bean
    public GroupedOpenApi experienceGroup() {
        return GroupedOpenApi.builder()
            .group("2-experience")
            .displayName("Experience")
            .pathsToMatch("/api/experience/**", "/api/experience")
            .build();
    }

    /**
     * Groups all /api/projects/** endpoints under the "Projects" section.
     *
     * @return grouped API definition for the project feature
     */
    @Bean
    public GroupedOpenApi projectsGroup() {
        return GroupedOpenApi.builder()
            .group("3-projects")
            .displayName("Projects")
            .pathsToMatch("/api/projects/**", "/api/projects")
            .build();
    }

    /**
     * Groups all /api/skills/** endpoints under the "Skills" section.
     *
     * @return grouped API definition for the skill feature
     */
    @Bean
    public GroupedOpenApi skillsGroup() {
        return GroupedOpenApi.builder()
            .group("4-skills")
            .displayName("Skills")
            .pathsToMatch("/api/skills/**", "/api/skills")
            .build();
    }

    /**
     * Groups all /api/education/** endpoints under the "Education" section.
     *
     * @return grouped API definition for the education feature
     */
    @Bean
    public GroupedOpenApi educationGroup() {
        return GroupedOpenApi.builder()
            .group("5-education")
            .displayName("Education")
            .pathsToMatch("/api/education/**", "/api/education")
            .build();
    }

    /**
     * Groups all /api/features/** endpoints under the "Features" section.
     *
     * @return grouped API definition for the features feature
     */
    @Bean
    public GroupedOpenApi featuresGroup() {
        return GroupedOpenApi.builder()
            .group("6-features")
            .displayName("Features")
            .pathsToMatch("/api/features/**", "/api/features")
            .build();
    }

    /**
     * Shows all endpoints in a single view (default).
     *
     * @return grouped API definition for all endpoints
     */
    @Bean
    public GroupedOpenApi allGroup() {
        return GroupedOpenApi.builder()
            .group("0-all")
            .displayName("All Endpoints")
            .pathsToMatch("/api/**")
            .build();
    }
}
