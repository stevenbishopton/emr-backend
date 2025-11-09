package hospital.emr.patient.services;

import hospital.emr.patient.dtos.AdmissionPrescriptionEntryDTO;
import hospital.emr.patient.entities.AdmissionPrescriptionEntry;
import hospital.emr.patient.mapper.AdmissionPrescriptionEntryMapper;
import hospital.emr.patient.repos.AdmissionPrescriptionEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdmissionPrescriptionEntryService {
    private final AdmissionPrescriptionEntryRepository admissionPrescriptionEntryRepository;
    private final AdmissionPrescriptionEntryMapper admissionPrescriptionEntryMapper;

//    public AdmissionPrescriptionEntryDTO createPrescriptionEntry(AdmissionPrescriptionEntryDTO dto) {
//        AdmissionPrescriptionEntry entity = admissionPrescriptionEntryMapper.toEntity(dto);
//        AdmissionPrescriptionEntry savedEntity = admissionPrescriptionEntryRepository.save(entity);
//        return admissionPrescriptionEntryMapper.toDto(savedEntity);
//    }

    public List<AdmissionPrescriptionEntryDTO> createPrescriptionEntries(List<AdmissionPrescriptionEntryDTO> dtos) {
        List<AdmissionPrescriptionEntry> entities = dtos.stream()
                .map(admissionPrescriptionEntryMapper::toEntity)
                .collect(Collectors.toList());
        List<AdmissionPrescriptionEntry> savedEntities = admissionPrescriptionEntryRepository.saveAll(entities);
        return savedEntities.stream()
                .map(admissionPrescriptionEntryMapper::toDto)
                .collect(Collectors.toList());
    }

    public AdmissionPrescriptionEntryDTO getPrescriptionEntryById(Long id) {
        Optional<AdmissionPrescriptionEntry> optionalEntry = admissionPrescriptionEntryRepository.findById(id);
        return optionalEntry
                .map(admissionPrescriptionEntryMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prescription entry not found with ID: " + id));
    }

    public void deletePrescriptionEntry(Long id) {
        if (!admissionPrescriptionEntryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Prescription entry not found with ID: " + id);
        }
        admissionPrescriptionEntryRepository.deleteById(id);
    }

    public AdmissionPrescriptionEntryDTO updatePrescriptionEntry(Long id, AdmissionPrescriptionEntryDTO dto) {
        return admissionPrescriptionEntryRepository.findById(id)
                .map(existingEntry -> {
                    admissionPrescriptionEntryMapper.updateEntityFromDto(dto, existingEntry);
                    AdmissionPrescriptionEntry updatedEntry = admissionPrescriptionEntryRepository.save(existingEntry);
                    return admissionPrescriptionEntryMapper.toDto(updatedEntry);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prescription entry not found with ID: " + id));
    }
}