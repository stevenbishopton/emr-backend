package hospital.emr.patient.services;

import hospital.emr.patient.dtos.AdmissionPrescriptionEntryDTO;
import hospital.emr.patient.dtos.PrescriptionCellUpdateDTO;
import hospital.emr.patient.dtos.PrescriptionChartDTO;
import hospital.emr.patient.entities.Admission;
import hospital.emr.patient.entities.AdmissionPrescriptionEntry;
import hospital.emr.patient.entities.PrescriptionChart;
import hospital.emr.patient.mapper.AdmissionPrescriptionEntryMapper;
import hospital.emr.patient.mapper.PrescriptionChartMapper;
import hospital.emr.patient.repos.AdmissionPrescriptionEntryRepository;
import hospital.emr.patient.repos.AdmissionRepository;
import hospital.emr.patient.repos.PrescriptionChartRepository;
import hospital.emr.pharmacy.entities.Item;
import hospital.emr.pharmacy.exceptions.ItemNotFoundException;
import hospital.emr.pharmacy.repos.ItemRepository;
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
    private final ItemRepository itemRepository;
    private final AdmissionRepository admissionRepository;

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
        PrescriptionChart chart = getOrCreateChart(entryDTO.getAdmissionId());

        AdmissionPrescriptionEntry entry = entryMapper.toEntity(entryDTO);
        entry.setId(null);
        entry.setChart(chart);

        // Set itemName from itemId if not already set
        if (entry.getItemId() != null && (entry.getItemName() == null || entry.getItemName().trim().isEmpty())) {
            Item item = itemRepository.findById(entry.getItemId())
                    .orElseThrow(() -> new ItemNotFoundException("Item not found with ID: " + entry.getItemId()));
            entry.setItemName(item.getName());
        }

        // Persist the entry explicitly so an id is assigned even before the chart flush
        AdmissionPrescriptionEntry savedEntry = admissionPrescriptionEntryRepository.save(entry);

        if (chart.getEntries() == null) {
            chart.setEntries(new ArrayList<>());
        }
        if (chart.getEntries().stream().noneMatch(e -> e.getId() != null && e.getId().equals(savedEntry.getId()))) {
            chart.getEntries().add(savedEntry);
        }
        chart.setUpdatedAt(LocalDateTime.now());
        prescriptionChartRepository.save(chart);

        return entryMapper.toDto(savedEntry);
    }

    private PrescriptionChart getOrCreateChart(Long admissionId) {
        if (admissionId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "admissionId is required");
        }
        return prescriptionChartRepository.findByAdmissionId(admissionId)
                .orElseGet(() -> {
                    Admission admission = admissionRepository.findById(admissionId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Admission not found with ID: " + admissionId));
                    PrescriptionChart newChart = PrescriptionChart.builder()
                            .admission(admission)
                            .entries(new ArrayList<>())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return prescriptionChartRepository.save(newChart);
                });
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