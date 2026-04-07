package hospital.emr.lab.services;

import hospital.emr.lab.dtos.CreateLabTestResultRequest;
import hospital.emr.lab.dtos.LabTestDTO;
import hospital.emr.lab.dtos.LabTestResultDTO;
import hospital.emr.lab.dtos.UpdateLabTestResultRequest;
import hospital.emr.lab.entities.LabTestResult;
import hospital.emr.lab.enums.TestStatus;
import hospital.emr.lab.repos.LabTestRepository;
import hospital.emr.lab.repos.LabTestResultRepository;
import hospital.emr.patient.entities.MedicalHistory;
import hospital.emr.patient.repos.MedicalHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing lab test results
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LabResultService {

    private final LabTestResultRepository resultRepository;
    private final LabTestRepository testRepository;
    private final LabTestRegistryService testRegistryService;
    private final MedicalHistoryRepository medicalHistoryRepository;

    /**
     * Create a new lab test result
     */
    @Transactional
    public LabTestResultDTO createResult(CreateLabTestResultRequest request) {
        log.info("Creating lab test result for patient: {}", request.getPatientId());
        
        // Validate that all test IDs exist
        for (Long testId : request.getTestIds()) {
            if (!testRepository.existsById(testId)) {
                throw new RuntimeException("Lab test not found with id: " + testId);
            }
        }

        LabTestResult result = new LabTestResult();
        result.setPatientId(request.getPatientId());
        result.setVisitId(request.getVisitId());
        result.setTestIds(request.getTestIds());
        result.setResults(request.getResults());
        result.setInterpretation(request.getInterpretation());
        result.setComments(request.getComments());
        result.setCarriedOutBy(request.getCarriedOutBy());
        result.setRequestedBy(request.getRequestedBy());
        result.setStatus(TestStatus.PENDING);

        // Link to medical history
        MedicalHistory medicalHistory = medicalHistoryRepository.findByPatient_Id(request.getPatientId());
        if (medicalHistory != null) {
            result.setMedicalHistory(medicalHistory);
        }

        LabTestResult saved = resultRepository.save(result);
        log.info("Lab test result created with id: {}", saved.getId());
        
        return toDTO(saved);
    }

    /**
     * Update a lab test result
     */
    @Transactional
    public LabTestResultDTO updateResult(Long resultId, UpdateLabTestResultRequest request) {
        log.info("Updating lab test result: {}", resultId);
        
        LabTestResult result = resultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Lab test result not found with id: " + resultId));
        
        if (request.getResults() != null) {
            result.setResults(request.getResults());
        }
        if (request.getInterpretation() != null) {
            result.setInterpretation(request.getInterpretation());
        }
        if (request.getComments() != null) {
            result.setComments(request.getComments());
        }
        if (request.getStatus() != null) {
            result.setStatus(request.getStatus());
            
            // Update timestamps based on status
            if (request.getStatus() == TestStatus.COMPLETED && result.getCompletedAt() == null) {
                result.setCompletedAt(LocalDateTime.now());
            }

        }
        
        LabTestResult saved = resultRepository.save(result);
        log.info("Lab test result updated: {}", resultId);
        
        return toDTO(saved);
    }

    /**
     * Get result by ID
     */
    @Transactional(readOnly = true)
    public LabTestResultDTO getResultById(Long resultId) {
        log.info("Fetching lab test result: {}", resultId);
        LabTestResult result = resultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Lab test result not found with id: " + resultId));
        return toDTO(result);
    }

    /**
     * Get all results for a patient
     */
    @Transactional(readOnly = true)
    public List<LabTestResultDTO> getResultsByPatientId(Long patientId) {
        log.info("Fetching lab test results for patient: {}", patientId);
        return resultRepository.findByPatientId(patientId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all results for a visit
     */
    @Transactional(readOnly = true)
    public List<LabTestResultDTO> getResultsByVisitId(Long visitId) {
        log.info("Fetching lab test results for visit: {}", visitId);
        return resultRepository.findByVisitId(visitId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get results by status
     */
    @Transactional(readOnly = true)
    public List<LabTestResultDTO> getResultsByStatus(TestStatus status) {
        log.info("Fetching lab test results with status: {}", status);
        return resultRepository.findByStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get pending results
     */
    @Transactional(readOnly = true)
    public List<LabTestResultDTO> getPendingResults() {
        log.info("Fetching pending lab test results");
        List<TestStatus> pendingStatuses = List.of(TestStatus.PENDING, TestStatus.IN_PROGRESS);
        return resultRepository.findPendingResults(pendingStatuses).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert entity to DTO
     */
    private LabTestResultDTO toDTO(LabTestResult result) {
        LabTestResultDTO dto = new LabTestResultDTO();
        dto.setId(result.getId());
        dto.setPatientId(result.getPatientId());
        dto.setVisitId(result.getVisitId());
        dto.setCarriedOutBy(result.getCarriedOutBy());
        dto.setTestIds(result.getTestIds());
        dto.setResults(result.getResults());
        dto.setInterpretation(result.getInterpretation());
        dto.setComments(result.getComments());
        dto.setStatus(result.getStatus());
        dto.setRequestedBy(result.getRequestedBy());
        dto.setCreatedAt(result.getCreatedAt());
        dto.setUpdatedAt(result.getUpdatedAt());
        dto.setCompletedAt(result.getCompletedAt());

        // Populate test details
        if (result.getTestIds() != null && !result.getTestIds().isEmpty()) {
            List<LabTestDTO> tests = result.getTestIds().stream()
                    .map(testId -> {
                        try {
                            return testRegistryService.getTestById(testId);
                        } catch (Exception e) {
                            log.warn("Could not fetch test with id: {}", testId);
                            return null;
                        }
                    })
                    .filter(test -> test != null)
                    .collect(Collectors.toList());
            dto.setTests(tests);
        }
        
        return dto;
    }
}
