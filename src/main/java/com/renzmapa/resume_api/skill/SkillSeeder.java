package com.renzmapa.resume_api.skill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * SkillSeeder
 *
 * Populates the skills table on first boot so a fresh database is never empty.
 * The run is idempotent: if any skill already exists the seeder does nothing,
 * which keeps edits made through the API from being overwritten on restart.
 */
@Component
public class SkillSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SkillSeeder.class);

    private final SkillRepository skillRepository;

    public SkillSeeder(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    /**
     * Seeds the default skill list if the table is empty.
     * Categories are seeded in the order they are added below, and sortOrder runs
     * unbroken from 1 across the whole list, so the grouped response comes back
     * in exactly this order.
     *
     * @param args command line arguments passed to the application (unused)
     */
    @Override
    public void run(String... args) {
        long existing = skillRepository.count();
        if (existing > 0) {
            log.info("Skills already seeded ({} rows) — skipping.", existing);
            return;
        }

        List<Skill> skills = new ArrayList<>();
        addCategory(skills, "Programming Languages",
            "Java", "JavaScript / TypeScript", "Python", "C", "C++", "C#", "PHP");
        addCategory(skills, "Frameworks",
            "Next.js", "NestJS", "Spring Boot", "React", "FastAPI", "Django", "Rails", "Tailwind");
        addCategory(skills, "Tools & Platforms",
            "AWS", "Docker", "Git", "Cisco Packet Tracer", "VS Code");
        addCategory(skills, "AI Tools",
            "Claude Code", "Antigravity");

        skillRepository.saveAll(skills);

        log.info("Seeded {} skills.", skills.size());
    }

    /**
     * Appends one category's skills to the batch, continuing the running sortOrder
     * so it counts up from 1 across every category in call order.
     */
    private void addCategory(List<Skill> batch, String category, String... names) {
        for (String name : names) {
            batch.add(new Skill(category, name, batch.size() + 1));
        }
    }
}
