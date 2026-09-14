package com.renzmapa.resume_api.experience;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    // JpaRepository gives you findAll(), findById(), save(), delete() for free

    /**
     * Returns every experience row sorted by its sortOrder column, ascending.
     * Spring Data auto-generates: SELECT * FROM experiences ORDER BY sort_order ASC
     */
    List<Experience> findAllByOrderBySortOrderAsc();
}
