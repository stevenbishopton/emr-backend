package hospital.emr.radiograph.services;

import hospital.emr.radiograph.dtos.RadiographDTO;
import hospital.emr.radiograph.entities.Radiograph;
import hospital.emr.radiograph.enums.RadiographStatus;
import hospital.emr.radiograph.mappers.RadiographMapper;
import hospital.emr.radiograph.repos.RadiographRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RadiographServiceImpl implements RadiographService {

    private final RadiographRepository radiographRepository;
    private final RadiographMapper radiographMapper;

    @Override
    public RadiographDTO createRadiograph(RadiographDTO radiographDTO) {
        log.info("Creating new radiograph: {}", radiographDTO.getRadiographName());
        
        Radiograph radiograph = radiographMapper.toEntity(radiographDTO);
        
        // Set default status if not provided
        if (radiograph.getStatus() == null) {
            radiograph.setStatus(RadiographStatus.REQUESTED);
        }
        
        Radiograph savedRadiograph = radiographRepository.save(radiograph);
        log.info("Created radiograph with id: {}", savedRadiograph.getId());
        
        return radiographMapper.toDto(savedRadiograph);
    }

    @Override
    public RadiographDTO updateRadiograph(Long id, RadiographDTO radiographDTO) {
        log.info("Updating radiograph: {}", id);
        
        Radiograph existingRadiograph = radiographRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Radiograph not found with id: " + id));

        // Update all fields including new ones
        existingRadiograph.setPatientId(radiographDTO.getPatientId());
        existingRadiograph.setVisitId(radiographDTO.getVisitId());
        existingRadiograph.setRadiographType(radiographDTO.getRadiographType());
        existingRadiograph.setRadiographName(radiographDTO.getRadiographName());
        existingRadiograph.setStatus(radiographDTO.getStatus());
        existingRadiograph.setCarriedOutBy(radiographDTO.getCarriedOutBy());
        existingRadiograph.setInterpretation(radiographDTO.getInterpretation());
        existingRadiograph.setComments(radiographDTO.getComments());
        existingRadiograph.setOrderDate(radiographDTO.getOrderDate());
        existingRadiograph.setResultDate(radiographDTO.getResultDate());
        
        // Update new fields
        existingRadiograph.setScheduledDate(radiographDTO.getScheduledDate());
        existingRadiograph.setScheduledTime(radiographDTO.getScheduledTime());
        existingRadiograph.setTechnicianNotes(radiographDTO.getTechnicianNotes());
        existingRadiograph.setRadiologistNotes(radiographDTO.getRadiologistNotes());
        existingRadiograph.setReportUrl(radiographDTO.getReportUrl());
        existingRadiograph.setImageUrl(radiographDTO.getImageUrl());
        existingRadiograph.setRequestedBy(radiographDTO.getRequestedBy());
        existingRadiograph.setDepartmentId(radiographDTO.getDepartmentId());

        Radiograph updatedRadiograph = radiographRepository.save(existingRadiograph);
        log.info("Updated radiograph: {}", id);
        
        return radiographMapper.toDto(updatedRadiograph);
    }

    @Override
    @Transactional(readOnly = true)
    public RadiographDTO getRadiographById(Long id) {
        log.debug("Fetching radiograph: {}", id);
        Radiograph radiograph = radiographRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Radiograph not found with id: " + id));
        return radiographMapper.toDto(radiograph);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographDTO> getAllRadiographs() {
        log.debug("Fetching all radiographs");
        return radiographRepository.findAll()
                .stream()
                .map(radiographMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographDTO> getRadiographsByPatientId(Long patientId) {
        log.debug("Fetching radiographs for patient: {}", patientId);
        return radiographRepository.findByPatientId(patientId)
                .stream()
                .map(radiographMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographDTO> getRadiographsByVisitId(Long visitId) {
        log.debug("Fetching radiographs for visit: {}", visitId);
        return radiographRepository.findByVisitId(visitId)
                .stream()
                .map(radiographMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographDTO> getRadiographsByMedicalHistoryId(Long medicalHistoryId) {
        log.debug("Fetching radiographs for medical history: {}", medicalHistoryId);
        return radiographRepository.findByMedicalHistoryId(medicalHistoryId)
                .stream()
                .map(radiographMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRadiograph(Long id) {
        log.info("Deleting radiograph: {}", id);
        if (!radiographRepository.existsById(id)) {
            throw new RuntimeException("Radiograph not found with id: " + id);
        }
        radiographRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return radiographRepository.existsById(id);
    }
}