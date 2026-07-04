package com.renzmapa.resume_api.profile;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // JpaRepository gives you findAll(), findById(), save(), delete() for free

    /**
     * Returns the profile with the highest ID (most recently inserted row).
     * Spring Data auto-generates: SELECT * FROM profiles ORDER BY id DESC LIMIT 1
     */
    Optional<Profile> findTopByOrderByIdDesc();
}
