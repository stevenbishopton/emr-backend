package hospital.emr.common.services;

import hospital.emr.common.dtos.DepartmentDTO;
import hospital.emr.common.entities.Department;
import hospital.emr.common.exceptions.DepartmentNotFoundException;
import hospital.emr.common.mappers.DepartmentMapper;
import hospital.emr.common.repos.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        log.info("Creating dept");
        Department department = departmentMapper.toEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        log.info("Dept created");
        return departmentMapper.toDto(savedDepartment);
    }


    @Transactional(readOnly = true)
    public List<DepartmentDTO> findAllDepartments() {
        log.info("Finding departments..");
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentDTO findDepartmentById(Long id) {
        log.info("Finding department...");
        return departmentRepository.findById(id)
                .map(departmentMapper::toDto)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with ID: " + id));
    }


    @Transactional
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with ID: " + id));
        departmentMapper.updateFromDto(departmentDTO, existingDepartment);
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return departmentMapper.toDto(updatedDepartment);
    }


    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new DepartmentNotFoundException("Department not found with ID: " + id);
        }
        departmentRepository.deleteById(id);
    }
}
