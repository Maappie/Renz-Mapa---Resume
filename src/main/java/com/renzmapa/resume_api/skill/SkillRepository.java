package com.renzmapa.resume_api.skill;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    // JpaRepository gives you findAll(), findById(), save(), delete() for free

    /**
     * Returns every skill sorted by its display order, lowest first.
     * Spring Data auto-generates: SELECT * FROM skills ORDER BY sort_order ASC
     */
    List<Skill> findAllByOrderBySortOrderAsc();
}
