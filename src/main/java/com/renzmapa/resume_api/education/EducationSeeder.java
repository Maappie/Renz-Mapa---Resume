package com.renzmapa.resume_api.education;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * EducationSeeder
 *
 * Populates the education table on startup so a fresh database is never empty.
 * The run is idempotent: if any rows already exist the seeder exits immediately,
 * which means manual edits made through the API are never overwritten on restart.
 */
@Component
public class EducationSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(EducationSeeder.class);

    private final EducationRepository educationRepository;

    public EducationSeeder(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    /**
     * Inserts the default education entries if the table is empty.
     *
     * @param args command line arguments passed to the application (unused)
     */
    @Override
    public void run(String... args) {
        if (educationRepository.count() > 0) return;

        List<Education> seed = List.of(plm());
        educationRepository.saveAll(seed);

        log.info("Seeded {} education entries into an empty education table.", seed.size());
    }

    /**
     * Builds the Pamantasan ng Lungsod ng Maynila education entry.
     *
     * @return the seed Education for the undergraduate degree
     */
    private Education plm() {
        return new Education(
            "Pamantasan ng Lungsod ng Maynila",
            "Bachelor of Science in Computer Engineering",
            "Cum Laude",
            "2022 — 2026",
            "Computer Engineering with a focus on software systems, embedded development, and networking — graduated Cum Laude.",
            1
        );
    }
}
