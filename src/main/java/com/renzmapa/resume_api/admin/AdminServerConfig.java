package com.renzmapa.resume_api.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.context.annotation.Configuration;

/**
 * AdminServerConfig
 *
 * Enables the embedded Spring Boot Admin Server UI for this application.
 * The dashboard is accessible at: http://localhost:8080/admin
 *
 * This application registers itself as a client via spring-boot-admin-starter-client,
 * so it appears in the dashboard automatically on startup.
 */
@Configuration
@EnableAdminServer
public class AdminServerConfig {
    // No additional beans required.
    // All configuration is handled via application.properties.
}
