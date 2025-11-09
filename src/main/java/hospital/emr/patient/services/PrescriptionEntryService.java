package hospital.emr.patient.services;

import hospital.emr.patient.dtos.PrescriptionEntryDTO;
import hospital.emr.patient.entities.PrescriptionEntry;
import hospital.emr.patient.mapper.PrescriptionEntryMapper;
import hospital.emr.patient.repos.PrescriptionEntryRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionEntryService {

    private final PrescriptionEntryRepository prescriptionEntryRepository;
    private final PrescriptionEntryMapper prescriptionEntryMapper;

    @Transactional
    public List<PrescriptionEntryDTO> createPrescriptionEntries(List<PrescriptionEntryDTO> dtos) {
        List<PrescriptionEntry> entries = dtos.stream()
                .map(prescriptionEntryMapper::toEntity)
                .collect(Collectors.toList());

        List<PrescriptionEntry> savedEntries = prescriptionEntryRepository.saveAll(entries);

        return savedEntries.stream()
                .map(prescriptionEntryMapper::toDto)
                .collect(Collectors.toList());
    }


}