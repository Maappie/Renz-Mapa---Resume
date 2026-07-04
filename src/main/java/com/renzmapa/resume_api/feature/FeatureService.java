package com.renzmapa.resume_api.feature;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for the Feature resource.
 *
 * <p>This class owns all business logic. Controllers must NEVER contain
 * business logic — they only handle HTTP concerns (parsing, response codes).
 *
 * <p>When a database is introduced, this class is the only layer that changes.
 * The controller and model remain completely untouched.
 */
@Service
public class FeatureService {

    /**
     * Returns the full list of available features.
     *
     * <p>Replace the hardcoded list with a repository call when a database
     * is wired up: {@code return featureRepository.findAll();}
     *
     * @return list of {@link Feature} objects
     */
    public List<Feature> getAll() {
        return List.of(
            new Feature(1L, "Sample Feature", "This is a placeholder description.", List.of("tag1", "tag2"))
        );
    }

    /**
     * Returns a single feature by its ID.
     *
     * <p>Returns {@code null} if not found; the controller is responsible
     * for translating that into the appropriate HTTP response (e.g. 404).
     *
     * @param id the resource identifier
     * @return the matching {@link Feature}, or {@code null}
     */
    public Feature getById(Long id) {
        return getAll().stream()
            .filter(f -> f.id().equals(id))
            .findFirst()
            .orElse(null);
    }
}
