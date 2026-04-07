package hospital.emr.lab.services;

import hospital.emr.lab.dtos.LabTestDTO;
import hospital.emr.lab.entities.LabTest;
import hospital.emr.lab.enums.SampleType;
import hospital.emr.lab.enums.TestCategory;
import hospital.emr.lab.repos.LabTestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to provide access to all lab tests for selection
 * Acts as a registry/catalog of available tests
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LabTestRegistryService {

    private final LabTestRepository labTestRepository;

    /**
     * Get all available lab tests
     */
    @Transactional(readOnly = true)
    public List<LabTestDTO> getAllTests() {
        log.info("Fetching all lab tests");
        return labTestRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get test by ID
     */
    @Transactional(readOnly = true)
    public LabTestDTO getTestById(Long id) {
        log.info("Fetching lab test with id: {}", id);
        LabTest test = labTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found with id: " + id));
        return toDTO(test);
    }

    /**
     * Get tests by category
     */
    @Transactional(readOnly = true)
    public List<LabTestDTO> getTestsByCategory(TestCategory category) {
        log.info("Fetching lab tests for category: {}", category);
        return labTestRepository.findByCategory(category).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get tests by sample type
     */
    @Transactional(readOnly = true)
    public List<LabTestDTO> getTestsBySampleType(SampleType sampleType) {
        log.info("Fetching lab tests for sample type: {}", sampleType);
        return labTestRepository.findBySampleType(sampleType).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search tests by name
     */
    @Transactional(readOnly = true)
    public List<LabTestDTO> searchTests(String searchTerm) {
        log.info("Searching lab tests with term: {}", searchTerm);
        return labTestRepository.searchByName(searchTerm).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get tests grouped by category
     */
    @Transactional(readOnly = true)
    public List<LabTestDTO> getTestsByCategoryAndSampleType(TestCategory category, SampleType sampleType) {
        log.info("Fetching lab tests for category: {} and sample type: {}", category, sampleType);
        return labTestRepository.findByCategoryAndSampleType(category, sampleType).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert LabTest entity to DTO
     */
    private LabTestDTO toDTO(LabTest test) {
        LabTestDTO dto = new LabTestDTO();
        dto.setId(test.getId());
        dto.setName(test.getName());
        dto.setPrice(test.getPrice());
        dto.setCategory(test.getCategory());
        dto.setSampleType(test.getSampleType());
        dto.setDescription(test.getDescription());
        
        // Get reference range using reflection or by checking test type
        // For now, we'll need to handle this per test type
        // This is a simplified version - in production, you might want to use a more sophisticated approach
        try {
            java.lang.reflect.Method getReferenceRange = test.getClass().getMethod("getReferenceRange");
            String referenceRange = (String) getReferenceRange.invoke(test);
            dto.setReferenceRange(referenceRange);
        } catch (Exception e) {
            log.warn("Could not get reference range for test: {}", test.getName());
        }
        
        return dto;
    }
}
