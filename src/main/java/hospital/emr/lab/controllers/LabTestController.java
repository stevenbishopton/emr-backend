package hospital.emr.lab.controllers;

import hospital.emr.lab.dtos.LabAnalyteTemplateDTO;
import hospital.emr.lab.dtos.LabTestDTO;
import hospital.emr.lab.enums.SampleType;
import hospital.emr.lab.enums.TestCategory;
import hospital.emr.lab.services.LabAnalyteTemplateService;
import hospital.emr.lab.services.LabTestRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final LabAnalyteTemplateService analyteTemplateService;

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

    /**
     * Get the analyte template (analytes, units, reference ranges) for a test
     */
    @GetMapping("/{id}/analytes")
    public ResponseEntity<List<LabAnalyteTemplateDTO>> getAnalyteTemplates(@PathVariable Long id) {
        return ResponseEntity.ok(analyteTemplateService.getForTest(id));
    }

    /**
     * Create a new catalog test (admin / lab personnel only)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LAB_PERSONNEL')")
    public ResponseEntity<LabTestDTO> createTest(@RequestBody LabTestDTO testDTO) {
        return new ResponseEntity<>(testRegistryService.createTest(testDTO), HttpStatus.CREATED);
    }

    /**
     * Update an existing catalog test (admin / lab personnel only)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LAB_PERSONNEL')")
    public ResponseEntity<LabTestDTO> updateTest(@PathVariable Long id, @RequestBody LabTestDTO testDTO) {
        return ResponseEntity.ok(testRegistryService.updateTest(id, testDTO));
    }

    /**
     * Delete a catalog test (admin / lab personnel only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LAB_PERSONNEL')")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        testRegistryService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }
}
