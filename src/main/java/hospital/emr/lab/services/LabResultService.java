package hospital.emr.lab.services;

import hospital.emr.lab.dtos.CreateLabTestResultRequest;
import hospital.emr.lab.dtos.LabTestDTO;
import hospital.emr.lab.dtos.LabTestResultDTO;
import hospital.emr.lab.dtos.UpdateLabTestResultRequest;
import hospital.emr.lab.entities.LabHistory;
import hospital.emr.lab.entities.LabTestResult;
import hospital.emr.lab.enums.TestStatus;
import hospital.emr.lab.repos.LabHistoryRepository;
import hospital.emr.lab.repos.LabTestRepository;
import hospital.emr.lab.repos.LabTestRequestRepository;
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
    private final LabHistoryRepository labHistoryRepository;
    private final LabTestRequestRepository labTestRequestRepository;

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
        
        // Determine status based on whether results have actual values
        TestStatus status = determineStatus(request);
        result.setStatus(status);

        // Link to medical history
        MedicalHistory medicalHistory = medicalHistoryRepository.findByPatient_Id(request.getPatientId());
        if (medicalHistory != null) {
            result.setMedicalHistory(medicalHistory);
        }

        // Set timestamps based on status
        if (status == TestStatus.COMPLETED) {
            result.setCompletedAt(LocalDateTime.now());
        } else if (status == TestStatus.IN_PROGRESS) {
            result.setCollectedAt(LocalDateTime.now());
        }

        LabTestResult saved = resultRepository.save(result);
        log.info("Lab test result created with id: {} and status: {}", saved.getId(), saved.getStatus());
        
        // Update corresponding LabTestRequest status
        updateLabTestRequestStatus(saved, request.getCarriedOutBy());
        
        // Create initial history entry
        LabHistory history = new LabHistory();
        history.setLabTestResultId(saved.getId());
        history.setStatus(saved.getStatus());
        history.setNotes("Lab test result created with status: " + saved.getStatus());
        history.setPerformedBy(1L);
        history.setPerformedByName(request.getCarriedOutBy() != null ? request.getCarriedOutBy() : request.getRequestedBy());
        labHistoryRepository.save(history);
        
        return toDTO(saved);
    }

    /**
     * Determine the appropriate status based on the request
     * - If status is explicitly provided, use it
     * - If results have actual values, mark as COMPLETED
     * - If results object exists but no values (just collected), mark as IN_PROGRESS
     * - If no results at all, mark as PENDING
     */
    private TestStatus determineStatus(CreateLabTestResultRequest request) {
        // If status is explicitly provided, use it
        if (request.getStatus() != null) {
            return request.getStatus();
        }
        
        // Check if we have results
        if (request.getResults() == null || request.getResults().isEmpty()) {
            return TestStatus.PENDING;
        }
        
        // Check if results contain actual values (not empty placeholders)
        boolean hasActualValues = false;
        try {
            String resultsStr = request.getResults();
            // Check for value fields with actual content
            hasActualValues = resultsStr.contains("\"value\":\"") && 
                              !resultsStr.contains("\"value\":\"\"") &&
                              !resultsStr.contains("\"value\":\"Sample collected - pending analysis\"") &&
                              !resultsStr.contains("\"value\":\"pending\"");
        } catch (Exception e) {
            // If parsing fails, assume we have values (conservative approach)
            hasActualValues = true;
        }
        
        return hasActualValues ? TestStatus.COMPLETED : TestStatus.IN_PROGRESS;
    }

    /**
     * Update a lab test result
     */
    @Transactional
    public LabTestResultDTO updateResult(Long resultId, UpdateLabTestResultRequest request) {
        log.info("Updating lab test result: {}", resultId);
        
        LabTestResult result = resultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Lab test result not found with id: " + resultId));
        
        TestStatus previousStatus = result.getStatus();
        
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
            if (request.getStatus() == TestStatus.IN_PROGRESS && result.getCollectedAt() == null) {
                result.setCollectedAt(LocalDateTime.now());
            }
        } else {
            // Auto-determine status from results if not explicitly provided
            TestStatus autoStatus = determineStatusFromUpdate(request, result);
            if (autoStatus != result.getStatus()) {
                result.setStatus(autoStatus);
                if (autoStatus == TestStatus.COMPLETED && result.getCompletedAt() == null) {
                    result.setCompletedAt(LocalDateTime.now());
                }
                if (autoStatus == TestStatus.IN_PROGRESS && result.getCollectedAt() == null) {
                    result.setCollectedAt(LocalDateTime.now());
                }
            }
        }
        
        LabTestResult saved = resultRepository.save(result);
        log.info("Lab test result updated: {}", resultId);
        
        // Update corresponding LabTestRequest status
        updateLabTestRequestStatus(saved, saved.getCarriedOutBy());
        
        // Create history entry for status change
        if (!saved.getStatus().equals(previousStatus)) {
            LabHistory history = new LabHistory();
            history.setLabTestResultId(saved.getId());
            history.setStatus(saved.getStatus());
            history.setPreviousStatus(previousStatus.name());
            history.setNotes("Status changed from " + previousStatus + " to " + saved.getStatus());
            history.setPerformedBy(1L);
            history.setPerformedByName(saved.getCarriedOutBy());
            labHistoryRepository.save(history);
        }
        
        return toDTO(saved);
    }

    /**
     * Auto-determine status from update request
     */
    private TestStatus determineStatusFromUpdate(UpdateLabTestResultRequest request, LabTestResult current) {
        String results = request.getResults() != null ? request.getResults() : current.getResults();
        
        if (results == null || results.isEmpty()) {
            return TestStatus.PENDING;
        }
        
        boolean hasActualValues = false;
        try {
            String resultsStr = results;
            hasActualValues = resultsStr.contains("\"value\":\"") && 
                              !resultsStr.contains("\"value\":\"\"") &&
                              !resultsStr.contains("\"value\":\"Sample collected - pending analysis\"") &&
                              !resultsStr.contains("\"value\":\"pending\"");
        } catch (Exception e) {
            hasActualValues = true;
        }
        
        return hasActualValues ? TestStatus.COMPLETED : TestStatus.IN_PROGRESS;
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
        dto.setCollectedAt(result.getCollectedAt());

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

    /**
     * Update corresponding LabTestRequest status when LabTestResult is created/updated
     */
    private void updateLabTestRequestStatus(LabTestResult result, String performedBy) {
        try {
            // Find LabTestRequest by patient and visit (matching the result)
            if (result.getPatientId() != null && result.getVisitId() != null) {
                List<hospital.emr.lab.entities.LabTestRequest> requests = labTestRequestRepository.findByPatientIdAndVisitId(
                    result.getPatientId(), 
                    result.getVisitId()
                );
                
                // Update status of matching requests
                for (hospital.emr.lab.entities.LabTestRequest request : requests) {
                    if (request.getStatus() != TestStatus.COMPLETED) {
                        request.setStatus(result.getStatus());
                        request.setUpdatedAt(LocalDateTime.now());
                        labTestRequestRepository.save(request);
                        log.info("Updated LabTestRequest {} status to {}", request.getId(), result.getStatus());
                        
                        // Create history entry for status change
                        LabHistory history = new LabHistory();
                        history.setLabTestRequestId(request.getId());
                        history.setStatus(result.getStatus());
                        history.setNotes("Status updated based on lab test result");
                        history.setPerformedBy(1L);
                        history.setPerformedByName(performedBy != null ? performedBy : "Lab Technician");
                        labHistoryRepository.save(history);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to update LabTestRequest status: {}", e.getMessage());
            // Don't throw - this is a secondary operation
        }
    }
}