package hospital.emr.ward.services;

import hospital.emr.patient.dtos.AdmissionDTO;
import hospital.emr.patient.entities.Admission;
import hospital.emr.patient.repos.AdmissionRepository;
import hospital.emr.ward.dtos.WardDTO;
import hospital.emr.ward.entities.Ward;
import hospital.emr.ward.mappers.WardMapper;
import hospital.emr.ward.repos.WardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WardService {
    private final WardMapper wardMapper;
    private final WardRepository wardRepository;
    private final AdmissionRepository admissionRepository;

    @Transactional
    public WardDTO createWard(WardDTO wardDto){
        Ward ward = wardMapper.toEntity(wardDto);
        wardRepository.save(ward);
        return wardMapper.toDto(ward);
    }

    public List<WardDTO> getAllWards(){
        List<Ward> wards = wardRepository.findAll();
        return wards.stream().map(wardMapper::toDto).collect(Collectors.toList());
    }

}
