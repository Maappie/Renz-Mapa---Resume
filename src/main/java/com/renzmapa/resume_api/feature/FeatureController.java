package com.renzmapa.resume_api.feature;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for the Feature resource.
 *
 * <p>Responsibilities of this class are strictly limited to:
 * <ul>
 *   <li>Mapping HTTP routes to service calls.</li>
 *   <li>Translating service results into appropriate HTTP responses.</li>
 *   <li>Input validation (if needed, via {@code @Valid}).</li>
 * </ul>
 *
 * <p>This controller is automatically prefixed with {@code /api} by
 * {@code WebConfig} — do NOT add it manually here.
 *
 * <p>Base URL: {@code /api/features}
 */
@RestController
@RequestMapping("/features")
@Tag(name = "Features")
public class FeatureController {

    private final FeatureService featureService;

    /**
     * Constructor injection is preferred over {@code @Autowired}.
     * It makes dependencies explicit and simplifies unit testing.
     */
    public FeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    /**
     * GET /api/features — returns all available features.
     *
     * @return {@code 200 OK} with a JSON array of features.
     */
    @Operation(
        summary = "List all features",
        description = "Returns every feature in the database."
    )
    @ApiResponse(responseCode = "200", description = "Array of features (may be empty)")
    @GetMapping
    public ResponseEntity<List<Feature>> getAll() {
        return ResponseEntity.ok(featureService.getAll());
    }

    /**
     * GET /api/features/{id} — returns a single feature by ID.
     *
     * @param id the feature identifier from the URL path
     * @return {@code 200 OK} with the feature, or {@code 404 Not Found}.
     */
    @Operation(
        summary = "Get feature by ID",
        description = "Fetch a single feature by its database ID."
    )
    @ApiResponse(responseCode = "200", description = "The requested feature")
    @ApiResponse(responseCode = "404", description = "Feature with that ID does not exist")
    @GetMapping("/{id}")
    public ResponseEntity<Feature> getById(
            @Parameter(description = "Feature ID", example = "1")
            @PathVariable Long id) {
        Feature feature = featureService.getById(id);

        if (feature == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(feature);
    }
}
