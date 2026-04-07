package hospital.emr.lab.controllers;

import hospital.emr.lab.dtos.LabTestDTO;
import hospital.emr.lab.enums.SampleType;
import hospital.emr.lab.enums.TestCategory;
import hospital.emr.lab.services.LabTestRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Lab Test Catalog
 * Provides endpoints for doctors to browse and select lab tests
 */
@RestController
@RequestMapping("/emr/lab/tests")
@RequiredArgsConstructor
public class LabTestController {

    private final LabTestRegistryService testRegistryService;

    /**
     * Get all available lab tests
     */
    @GetMapping
    public ResponseEntity<List<LabTestDTO>> getAllTests() {
        return ResponseEntity.ok(testRegistryService.getAllTests());
    }

    /**
     * Get test by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LabTestDTO> getTestById(@PathVariable Long id) {
        return ResponseEntity.ok(testRegistryService.getTestById(id));
    }

    /**
     * Get tests by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<LabTestDTO>> getTestsByCategory(@PathVariable TestCategory category) {
        return ResponseEntity.ok(testRegistryService.getTestsByCategory(category));
    }

    /**
     * Get tests by sample type
     */
    @GetMapping("/sample-type/{sampleType}")
    public ResponseEntity<List<LabTestDTO>> getTestsBySampleType(@PathVariable SampleType sampleType) {
        return ResponseEntity.ok(testRegistryService.getTestsBySampleType(sampleType));
    }

    /**
     * Search tests by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<LabTestDTO>> searchTests(@RequestParam String q) {
        return ResponseEntity.ok(testRegistryService.searchTests(q));
    }

    /**
     * Get tests by category and sample type
     */
    @GetMapping("/category/{category}/sample-type/{sampleType}")
    public ResponseEntity<List<LabTestDTO>> getTestsByCategoryAndSampleType(
            @PathVariable TestCategory category,
            @PathVariable SampleType sampleType) {
        return ResponseEntity.ok(testRegistryService.getTestsByCategoryAndSampleType(category, sampleType));
    }
}
