package hospital.emr.patient.services;

import hospital.emr.patient.dtos.MedicationAdministrationDTO;
import hospital.emr.patient.entities.MedicationAdministration;
import hospital.emr.patient.entities.PrescriptionEntry;
import hospital.emr.patient.mapper.MedicationAdministrationMapper;
import hospital.emr.patient.repos.MedicationAdministrationRepository;
import hospital.emr.patient.repos.PrescriptionEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicationAdministrationService {

    private final MedicationAdministrationRepository repository;
    private final PrescriptionEntryRepository prescriptionEntryRepository;
    private final MedicationAdministrationMapper mapper;

    @Transactional
    public MedicationAdministrationDTO record(MedicationAdministrationDTO dto) {
        if (dto.getPrescriptionEntryId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "prescriptionEntryId is required");
        }
        if (dto.getAdministrationDate() == null || dto.getAdministrationTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "administrationDate and administrationTime are required");
        }

        PrescriptionEntry entry = prescriptionEntryRepository.findById(dto.getPrescriptionEntryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prescription entry not found with ID: " + dto.getPrescriptionEntryId()));

        Optional<MedicationAdministration> existing =
                repository.findByPrescriptionEntryAndAdministrationDateAndAdministrationTime(
                        entry, dto.getAdministrationDate(), dto.getAdministrationTime());

        MedicationAdministration target = existing.orElseGet(
                () -> MedicationAdministration.builder().prescriptionEntry(entry).build());

        target.setAdministrationDate(dto.getAdministrationDate());
        target.setAdministrationTime(dto.getAdministrationTime());
        target.setAdministered(dto.getAdministered() != null ? dto.getAdministered() : Boolean.TRUE);
        target.setNotes(dto.getNotes());
        if (dto.getAdministeredBy() != null) {
            target.setAdministeredBy(dto.getAdministeredBy());
        }

        return mapper.toDto(repository.save(target));
    }

    @Transactional
    public MedicationAdministrationDTO update(Long id, MedicationAdministrationDTO dto) {
        MedicationAdministration entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Administration not found with ID: " + id));
        if (dto.getAdministered() != null) entity.setAdministered(dto.getAdministered());
        if (dto.getAdministeredBy() != null) entity.setAdministeredBy(dto.getAdministeredBy());
        if (dto.getNotes() != null) entity.setNotes(dto.getNotes());
        if (dto.getAdministrationDate() != null) entity.setAdministrationDate(dto.getAdministrationDate());
        if (dto.getAdministrationTime() != null) entity.setAdministrationTime(dto.getAdministrationTime());
        return mapper.toDto(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<MedicationAdministrationDTO> byMedicalHistory(Long medicalHistoryId) {
        return repository
                .findByPrescriptionEntry_Prescription_MedicalHistory_IdOrderByAdministrationDateDesc(medicalHistoryId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MedicationAdministrationDTO> byEntry(Long entryId) {
        PrescriptionEntry entry = prescriptionEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prescription entry not found with ID: " + entryId));
        return repository.findByPrescriptionEntry(entry).stream().map(mapper::toDto).toList();
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Administration not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
