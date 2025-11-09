package hospital.emr.patient.services;

import hospital.emr.patient.dtos.PrescriptionAdministrationDTO;
import hospital.emr.patient.entities.AdmissionPrescriptionEntry;
import hospital.emr.patient.entities.PrescriptionAdministration;
import hospital.emr.patient.mapper.PrescriptionAdministrationMapper;
import hospital.emr.patient.repos.AdmissionPrescriptionEntryRepository;
import hospital.emr.patient.repos.PrescriptionAdministrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionAdministrationService {
    private final PrescriptionAdministrationRepository prescriptionAdministrationRepository;
    private final AdmissionPrescriptionEntryRepository admissionPrescriptionEntryRepository;
    private final PrescriptionAdministrationMapper administrationMapper;

    @Transactional
    public PrescriptionAdministrationDTO createAdministration(PrescriptionAdministrationDTO administrationDTO) {
        AdmissionPrescriptionEntry entry = admissionPrescriptionEntryRepository.findById(administrationDTO.getPrescriptionEntryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prescription entry not found with ID: " + administrationDTO.getPrescriptionEntryId()));

        // Check if administration already exists for this date/time
        Optional<PrescriptionAdministration> existingAdmin = prescriptionAdministrationRepository
                .findByPrescriptionEntryAndAdministrationDateAndAdministrationTime(
                        entry,
                        administrationDTO.getAdministrationDate(),
                        administrationDTO.getAdministrationTime()
                );

        if (existingAdmin.isPresent()) {
            // Update existing administration
            PrescriptionAdministration admin = existingAdmin.get();
            administrationMapper.updateEntityFromDto(administrationDTO, admin);
            PrescriptionAdministration saved = prescriptionAdministrationRepository.save(admin);
            return administrationMapper.toDto(saved);
        } else {
            // Create new administration
            PrescriptionAdministration administration = administrationMapper.toEntity(administrationDTO);
            administration.setPrescriptionEntry(entry);
            PrescriptionAdministration saved = prescriptionAdministrationRepository.save(administration);
            return administrationMapper.toDto(saved);
        }
    }

    @Transactional
    public PrescriptionAdministrationDTO updateAdministration(Long administrationId, PrescriptionAdministrationDTO administrationDTO) {
        PrescriptionAdministration administration = prescriptionAdministrationRepository.findById(administrationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Administration record not found with ID: " + administrationId));

        administrationMapper.updateEntityFromDto(administrationDTO, administration);
        PrescriptionAdministration saved = prescriptionAdministrationRepository.save(administration);
        return administrationMapper.toDto(saved);
    }

    @Transactional
    public void deleteAdministration(Long administrationId) {
        if (!prescriptionAdministrationRepository.existsById(administrationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Administration record not found with ID: " + administrationId);
        }
        prescriptionAdministrationRepository.deleteById(administrationId);
    }

    public List<PrescriptionAdministrationDTO> getAdministrationsByPrescriptionEntry(Long entryId) {
        AdmissionPrescriptionEntry entry = admissionPrescriptionEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prescription entry not found with ID: " + entryId));

        return prescriptionAdministrationRepository.findByPrescriptionEntry(entry)
                .stream()
                .map(administrationMapper::toDto)
                .toList();
    }

    public List<PrescriptionAdministrationDTO> getAdministrationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return prescriptionAdministrationRepository.findByAdministrationDateBetween(startDate, endDate)
                .stream()
                .map(administrationMapper::toDto)
                .toList();
    }

    @Transactional
    public PrescriptionAdministrationDTO toggleAdministration(Long entryId, LocalDate date, LocalTime time, String administeredBy) {
        AdmissionPrescriptionEntry entry = admissionPrescriptionEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prescription entry not found with ID: " + entryId));

        Optional<PrescriptionAdministration> existingAdmin = prescriptionAdministrationRepository
                .findByPrescriptionEntryAndAdministrationDateAndAdministrationTime(entry, date, time);

        if (existingAdmin.isPresent()) {
            // Toggle existing administration
            PrescriptionAdministration admin = existingAdmin.get();
            admin.setAdministered(!admin.getAdministered());
            admin.setAdministeredBy(administeredBy);
            PrescriptionAdministration saved = prescriptionAdministrationRepository.save(admin);
            return administrationMapper.toDto(saved);
        } else {
            // Create new administration as administered
            PrescriptionAdministration newAdmin = PrescriptionAdministration.builder()
                    .prescriptionEntry(entry)
                    .administrationDate(date)
                    .administrationTime(time)
                    .administered(true)
                    .administeredBy(administeredBy)
                    .notes("")
                    .build();
            PrescriptionAdministration saved = prescriptionAdministrationRepository.save(newAdmin);
            return administrationMapper.toDto(saved);
        }
    }
}