package hospital.emr.patient.services;

import hospital.emr.patient.dtos.AdmissionPrescriptionEntryDTO;
import hospital.emr.patient.dtos.PrescriptionCellUpdateDTO;
import hospital.emr.patient.dtos.PrescriptionChartDTO;
import hospital.emr.patient.entities.AdmissionPrescriptionEntry;
import hospital.emr.patient.entities.PrescriptionChart;
import hospital.emr.patient.mapper.AdmissionPrescriptionEntryMapper;
import hospital.emr.patient.mapper.PrescriptionChartMapper;
import hospital.emr.patient.repos.AdmissionPrescriptionEntryRepository;
import hospital.emr.patient.repos.PrescriptionChartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PrescriptionChartService {
    private final PrescriptionChartRepository prescriptionChartRepository;
    private final AdmissionPrescriptionEntryRepository admissionPrescriptionEntryRepository;
    private final AdmissionPrescriptionEntryMapper entryMapper;
    private final PrescriptionChartMapper chartMapper;

    // Cell update method for spreadsheet editing
    @Transactional
    public void updatePrescriptionCell(PrescriptionCellUpdateDTO updateRequest) {
        AdmissionPrescriptionEntry entry = admissionPrescriptionEntryRepository.findById(updateRequest.getEntryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prescription entry not found with ID: " + updateRequest.getEntryId()));

        String fieldName = updateRequest.getFieldName();

        // Handle field updates
        switch (fieldName) {
            case "itemName":
                entry.setItemName(updateRequest.getValue());
                break;
            case "instructions":
                entry.setInstructions(updateRequest.getValue());
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unknown field: " + updateRequest.getFieldName());
        }

        entry.setUpdatedAt(LocalDateTime.now());
        admissionPrescriptionEntryRepository.save(entry);
    }

    @Transactional
    public AdmissionPrescriptionEntryDTO addEntryToChart(AdmissionPrescriptionEntryDTO entryDTO) {
        PrescriptionChart chart = prescriptionChartRepository.findByAdmissionId(entryDTO.getAdmissionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No prescription chart found for admission ID: " + entryDTO.getAdmissionId()));

        AdmissionPrescriptionEntry entry = entryMapper.toEntity(entryDTO);
        entry.setChart(chart);

        // Initialize entries list if null
        if (chart.getEntries() == null) {
            chart.setEntries(new ArrayList<>());
        }

        chart.getEntries().add(entry);
        chart.setUpdatedAt(LocalDateTime.now());

        PrescriptionChart savedChart = prescriptionChartRepository.save(chart);

        // Find the saved entry to return with ID
        AdmissionPrescriptionEntry savedEntry = savedChart.getEntries().stream()
                .filter(e -> e.getItemName().equals(entry.getItemName()) &&
                        e.getInstructions().equals(entry.getInstructions()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to save prescription entry"));

        return entryMapper.toDto(savedEntry);
    }

    @Transactional(readOnly = true)
    public PrescriptionChartDTO getPrescriptionChartByAdmissionId(Long admissionId) {
        PrescriptionChart chart = prescriptionChartRepository.findByAdmissionId(admissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No prescription chart found for admission ID: " + admissionId));
        return chartMapper.toDto(chart);
    }

    @Transactional
    public void deletePrescriptionEntry(Long entryId) {
        if (!admissionPrescriptionEntryRepository.existsById(entryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Prescription entry not found with ID: " + entryId);
        }
        admissionPrescriptionEntryRepository.deleteById(entryId);
    }

    @Transactional
    public void deletePrescriptionChart(Long chartId) {
        if (!prescriptionChartRepository.existsById(chartId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Prescription chart not found with ID: " + chartId);
        }
        prescriptionChartRepository.deleteById(chartId);
    }
}