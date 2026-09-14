package com.renzmapa.resume_api.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * ProjectSeeder
 *
 * Populates the projects table on first boot so a fresh database is never empty.
 * The run is idempotent: if any project already exists the seeder does nothing,
 * which keeps edits made through the API from being overwritten on restart.
 */
@Component
public class ProjectSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ProjectSeeder.class);

    private final ProjectRepository projectRepository;

    public ProjectSeeder(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Seeds the default project list if the table is empty.
     *
     * @param args command line arguments passed to the application (unused)
     */
    @Override
    public void run(String... args) {
        long existing = projectRepository.count();
        if (existing > 0) {
            log.info("Projects already seeded ({} rows) — skipping.", existing);
            return;
        }

        List<Project> projects = List.of(
            lettuVault(),
            tanos(),
            virtualChurchAssistant()
        );
        projectRepository.saveAll(projects);

        log.info("Seeded {} projects.", projects.size());
    }

    private Project lettuVault() {
        return new Project(
            "LettuVault",
            "Lead Systems Architect & Developer",
            "2026",
            "A greenhouse that watches itself: sensor firmware on the plants, an MQTT spine carrying the "
                + "telemetry, and a control room that fits in your pocket.",
            List.of(
                "Engineered a modular IoT ecosystem for automated monitoring and control of high-value crop environments using ESP32, MQTT, and FastAPI.",
                "Built a secure telemetry pipeline with Pydantic validation and integrated YOLO-based AI vision for real-time crop status tracking.",
                "Developed a Flutter mobile app as a centralized dashboard for visualizing environmental trends and managing hardware setpoints remotely."
            ),
            List.of("ESP32", "MQTT", "FastAPI", "YOLO", "Flutter", "Pydantic"),
            "sprout",
            "lime",
            null,
            null,
            true,
            1
        );
    }

    private Project tanos() {
        return new Project(
            "TANOS",
            "OS Developer",
            "2024",
            "An operating system written from the kernel up in C# — built to make the parts a textbook "
                + "skips over, like file tables and auth, something you can actually poke at.",
            List.of(
                "Designed a lightweight, educational CLI operating system built on a secure C# monolithic kernel using Cosmos User Kit.",
                "Implemented FAT file management, user authentication, and a simplified number-based command interface."
            ),
            List.of("C#", "Cosmos User Kit", "FAT", "Kernel"),
            "terminal",
            "cyan",
            null,
            null,
            false,
            2
        );
    }

    private Project virtualChurchAssistant() {
        return new Project(
            "Virtual Church Assistant",
            "Web Developer",
            "2024",
            "Parish paperwork, digitized — service requests, approvals and records moved out of a logbook "
                + "and into a dashboard the staff actually use.",
            List.of(
                "Built a centralized web platform for the National Shrine of Saint Michael and the Archangels to digitize service requests and streamline workflows.",
                "Developed a responsive interface and admin dashboard to centralize data and automate request approvals."
            ),
            List.of("Web", "Admin Dashboard", "Workflow Automation"),
            "church",
            "violet",
            null,
            null,
            false,
            3
        );
    }
}
