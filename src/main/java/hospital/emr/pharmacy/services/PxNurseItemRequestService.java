package hospital.emr.pharmacy.services;

import hospital.emr.pharmacy.dto.PxNurseItemRequestDTO;
import hospital.emr.pharmacy.repos.PxNurseItemRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PxNurseItemRequestService {

    private final PxNurseItemRequestRepository pxNurseItemRequestRepository;

    @Transactional
    public PxNurseItemRequestDTO createPxNurseItemRequest(PxNurseItemRequestDTO pxNurseItemRequestDTO) {
        log.info("Creating new nurse item request for patient: {}", pxNurseItemRequestDTO.getPatientNames());
        PxNurseItemRequestDTO savedRequest = pxNurseItemRequestRepository.save(pxNurseItemRequestDTO);
        log.info("Successfully created pxNurseRequest with ID: {}", savedRequest.getId());
        return savedRequest;
    }

    @Transactional
    public Optional<PxNurseItemRequestDTO> updatePxNurseItemRequest(PxNurseItemRequestDTO pxNurseItemRequestDTO) {
        log.info("Updating nurse item request with ID: {}", pxNurseItemRequestDTO.getId());

        Optional<PxNurseItemRequestDTO> oldRequestDto =
                pxNurseItemRequestRepository.findById(pxNurseItemRequestDTO.getId());

        if (oldRequestDto.isPresent()) {
            PxNurseItemRequestDTO updatedRequest = pxNurseItemRequestRepository.save(pxNurseItemRequestDTO);
            log.info("Successfully updated pxNurseRequest with ID: {}", updatedRequest.getId());
            return Optional.of(updatedRequest);
        } else {
            log.warn("Nurse item request with ID: {} not found for update", pxNurseItemRequestDTO.getId());
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public List<PxNurseItemRequestDTO> getAllPxNurseItemRequests() {
        log.info("Fetching all nurse item requests");
        return pxNurseItemRequestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PxNurseItemRequestDTO> getPxNurseItemRequestById(Long id) {
        log.info("Fetching nurse item request by ID: {}", id);
        return pxNurseItemRequestRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<PxNurseItemRequestDTO> getPxNurseItemRequestsByPatientId(Long patientId) {
        log.info("Fetching nurse item requests for patient ID: {}", patientId);
        return pxNurseItemRequestRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<PxNurseItemRequestDTO> getPxNurseItemRequestsByVisitId(Long visitId) {
        log.info("Fetching nurse item requests for visit ID: {}", visitId);
        return pxNurseItemRequestRepository.findByVisitId(visitId);
    }

    @Transactional(readOnly = true)
    public List<PxNurseItemRequestDTO> getPxNurseItemRequestsByWardId(Long wardId) {
        log.info("Fetching nurse item requests for ward ID: {}", wardId);
        return pxNurseItemRequestRepository.findByWardId(wardId);
    }

    @Transactional
    public boolean deletePxNurseItemRequest(Long id) {
        log.info("Attempting to delete nurse item request with ID: {}", id);

        if (pxNurseItemRequestRepository.existsById(id)) {
            pxNurseItemRequestRepository.deleteById(id);
            log.info("Successfully deleted nurse item request with ID: {}", id);
            return true;
        } else {
            log.warn("Nurse item request with ID: {} not found for deletion", id);
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<PxNurseItemRequestDTO> getPxNurseItemRequestsByPatientCode(String patientCode) {
        log.info("Fetching nurse item requests for patient code: {}", patientCode);
        return pxNurseItemRequestRepository.findByPatientCode(patientCode);
    }
}