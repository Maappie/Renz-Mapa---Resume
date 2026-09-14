package com.renzmapa.resume_api.project;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // JpaRepository gives you findAll(), findById(), save(), delete() for free

    /**
     * Returns every project sorted by its display order, lowest first.
     * Spring Data auto-generates: SELECT * FROM projects ORDER BY sort_order ASC
     */
    List<Project> findAllByOrderBySortOrderAsc();
}
