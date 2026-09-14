package com.renzmapa.resume_api.education;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    // JpaRepository gives you findAll(), findById(), save(), delete() for free

    /**
     * Returns every education row sorted by its sortOrder column, ascending.
     * Spring Data auto-generates: SELECT * FROM education ORDER BY sort_order ASC
     */
    List<Education> findAllByOrderBySortOrderAsc();
}
