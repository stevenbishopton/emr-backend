package hospital.emr.radiograph.services;// hospital/emr/radiograph/service/RadiographService.java


import hospital.emr.radiograph.dtos.RadiographDTO;

import java.util.List;

public interface RadiographService {

    RadiographDTO createRadiograph(RadiographDTO radiographDTO);

    RadiographDTO updateRadiograph(Long id, RadiographDTO radiographDTO);

    RadiographDTO getRadiographById(Long id);

    List<RadiographDTO> getAllRadiographs();

    List<RadiographDTO> getRadiographsByPatientId(Long patientId);

    List<RadiographDTO> getRadiographsByVisitId(Long visitId);

    List<RadiographDTO> getRadiographsByMedicalHistoryId(Long medicalHistoryId);

    void deleteRadiograph(Long id);

    boolean existsById(Long id);
}