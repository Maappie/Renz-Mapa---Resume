package com.renzmapa.resume_api.experience;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * ExperienceSeeder
 *
 * Populates the experiences table on startup so a fresh database is never empty.
 * The run is idempotent: if any rows already exist the seeder exits immediately,
 * which means manual edits made through the API are never overwritten on restart.
 */
@Component
public class ExperienceSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ExperienceSeeder.class);

    private final ExperienceRepository experienceRepository;

    public ExperienceSeeder(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    /**
     * Inserts the default experience entries if the table is empty.
     *
     * @param args command line arguments passed to the application (unused)
     */
    @Override
    public void run(String... args) {
        if (experienceRepository.count() > 0) return;

        List<Experience> seed = List.of(contentLab(), quanbySolutions());
        experienceRepository.saveAll(seed);

        log.info("Seeded {} experience entries into an empty experiences table.", seed.size());
    }

    /**
     * Builds the Content Lab experience entry.
     *
     * @return the seed Experience for the current role
     */
    private Experience contentLab() {
        return new Experience(
            "Content Lab",
            "Junior Operations Developer",
            "June 2026 — Present",
            true,
            "Automating the day-to-day — internal tools, trackers, and integrations that keep campaign operations running smoothly.",
            new ArrayList<>(List.of(
                "Develop and maintain automations and custom solutions using Google Apps Script and Google Workspace tools to support day-to-day business and campaign operations.",
                "Manage and troubleshoot operational trackers and spreadsheets used for campaign management, resolving bugs across formulas, scripts, and automated workflows.",
                "Maintain integrations and data flow across operational platforms, including Google Sheets, Google Drive, Discord Bots, PandaDoc, Front, and Monday.com.",
                "Identify opportunities for automation to reduce manual work and improve existing operational processes.",
                "Collaborate with operations and cross-functional teams to troubleshoot technical issues and support the rollout of new internal tools and systems."
            )),
            new ArrayList<>(List.of(
                "Google Apps Script",
                "Google Workspace",
                "Discord Bots",
                "PandaDoc",
                "Front",
                "Monday.com"
            )),
            1
        );
    }

    /**
     * Builds the Quanby Solutions, Inc. experience entry.
     *
     * @return the seed Experience for the internship role
     */
    private Experience quanbySolutions() {
        return new Experience(
            "Quanby Solutions, Inc.",
            "Software Developer Intern",
            "February 2026 — March 2026",
            false,
            "Built a proof-of-concept enterprise platform (DOE-GSMS) to modernize the Department of Energy's internal operations.",
            new ArrayList<>(List.of(
                "Developed a Proof-of-Concept (POC) for DOE-GSMS, an enterprise platform designed to modernize the Department of Energy's internal operations.",
                "Architected the system within a Turborepo monorepo, integrating two core modules: a Fleet Management System (FMS) for motorpool tracking and a Computerized Maintenance Management System (CMMS) for vehicle maintenance.",
                "Built the platform across web and mobile using Next.js, NestJS, and Flutter, digitizing workflows for service and maintenance requests.",
                "Implemented secure digital signature capture and real-time data synchronization via Drizzle ORM."
            )),
            new ArrayList<>(List.of(
                "Next.js",
                "NestJS",
                "Flutter",
                "Turborepo",
                "Drizzle ORM",
                "TypeScript"
            )),
            2
        );
    }
}
