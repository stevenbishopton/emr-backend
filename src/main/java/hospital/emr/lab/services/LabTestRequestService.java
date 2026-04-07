package hospital.emr.lab.services;

import hospital.emr.lab.dtos.CreateLabTestRequestDTO;
import hospital.emr.lab.dtos.LabTestRequestResponseDTO;
import hospital.emr.lab.dtos.UpdateLabTestRequestDTO;
import hospital.emr.lab.entities.LabTestRequest;
import hospital.emr.lab.repos.LabTestRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LabTestRequestService {

    private final LabTestRequestRepository labTestRequestRepository; // ADD 'final' keyword

    public LabTestRequestResponseDTO createLabTestRequest(CreateLabTestRequestDTO requestDTO) {
        LabTestRequest labTestRequest = new LabTestRequest();
        mapCreateDtoToEntity(requestDTO, labTestRequest);

        labTestRequest.setCreatedAt(LocalDateTime.now());
        labTestRequest.setUpdatedAt(LocalDateTime.now());

        LabTestRequest savedRequest = labTestRequestRepository.save(labTestRequest);
        return mapEntityToResponseDto(savedRequest);
    }

    public LabTestRequestResponseDTO getLabTestRequestById(Long id) {
        LabTestRequest labTestRequest = labTestRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab Test Request not found with id: " + id));
        return mapEntityToResponseDto(labTestRequest);
    }

    public List<LabTestRequestResponseDTO> getAllLabTestRequests() {
        return labTestRequestRepository.findAll()
                .stream()
                .map(this::mapEntityToResponseDto)
                .collect(Collectors.toList());
    }

    public LabTestRequestResponseDTO updateLabTestRequest(Long id, UpdateLabTestRequestDTO requestDTO) {
        LabTestRequest existingLabTestRequest = labTestRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab Test Request not found with id: " + id));

        existingLabTestRequest.setPatientId(requestDTO.getPatientId());
        existingLabTestRequest.setVisitId(requestDTO.getVisitId());
        existingLabTestRequest.setMedicalHistoryId(requestDTO.getMedicalHistoryId());
        existingLabTestRequest.setLabTestIds(requestDTO.getLabTestIds());
        existingLabTestRequest.setRequestedBy(requestDTO.getRequestedBy());
        existingLabTestRequest.setComments(requestDTO.getComments());
        existingLabTestRequest.setUpdatedAt(LocalDateTime.now());

        LabTestRequest updatedRequest = labTestRequestRepository.save(existingLabTestRequest);
        return mapEntityToResponseDto(updatedRequest);
    }

    public void deleteLabTestRequest(Long id) {
        LabTestRequest labTestRequest = labTestRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab Test Request not found with id: " + id));
        labTestRequestRepository.delete(labTestRequest);
    }

    public List<LabTestRequestResponseDTO> getLabTestRequestsByPatientId(Long patientId) {
        return labTestRequestRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapEntityToResponseDto)
                .collect(Collectors.toList());
    }

    public List<LabTestRequestResponseDTO> getLabTestRequestsByVisitId(Long visitId) {
        return labTestRequestRepository.findByVisitId(visitId)
                .stream()
                .map(this::mapEntityToResponseDto)
                .collect(Collectors.toList());
    }

    public List<LabTestRequestResponseDTO> getLabTestRequestsByMedicalHistoryId(Long medicalHistoryId) {
        return labTestRequestRepository.findByMedicalHistoryId(medicalHistoryId)
                .stream()
                .map(this::mapEntityToResponseDto)
                .collect(Collectors.toList());
    }

    public List<LabTestRequestResponseDTO> getLabTestRequestsByRequestedBy(String requestedBy) {
        return labTestRequestRepository.findByRequestedBy(requestedBy)
                .stream()
                .map(this::mapEntityToResponseDto)
                .collect(Collectors.toList());
    }

    public List<LabTestRequestResponseDTO> getLabTestRequestsByLabTestId(Long testId) {
        return labTestRequestRepository.findByLabTestId(testId)
                .stream()
                .map(this::mapEntityToResponseDto)
                .collect(Collectors.toList());
    }

    public List<LabTestRequestResponseDTO> getLabTestRequestsByPatientAndVisit(Long patientId, Long visitId) {
        return labTestRequestRepository.findByPatientIdAndVisitId(patientId, visitId)
                .stream()
                .map(this::mapEntityToResponseDto)
                .collect(Collectors.toList());
    }

    // Helper methods
    private void mapCreateDtoToEntity(CreateLabTestRequestDTO dto, LabTestRequest entity) {
        entity.setPatientId(dto.getPatientId());
        entity.setVisitId(dto.getVisitId());
        entity.setMedicalHistoryId(dto.getMedicalHistoryId());
        entity.setLabTestIds(dto.getLabTestIds());
        entity.setRequestedBy(dto.getRequestedBy());
        entity.setComments(dto.getComments());
    }

    private LabTestRequestResponseDTO mapEntityToResponseDto(LabTestRequest entity) {
        return new LabTestRequestResponseDTO(
                entity.getId(),
                entity.getPatientId(),
                entity.getVisitId(),
                entity.getMedicalHistoryId(),
                entity.getLabTestIds(),
                entity.getRequestedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getComments()
        );
    }
}