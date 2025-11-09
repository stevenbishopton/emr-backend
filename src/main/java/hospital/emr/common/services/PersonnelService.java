package hospital.emr.common.services;

import hospital.emr.common.dtos.PersonnelDTO;
import hospital.emr.common.entities.*;
import hospital.emr.common.exceptions.PersonnelNotFoundException;
import hospital.emr.common.mappers.PersonnelMapper;
import hospital.emr.common.repos.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final PersonnelMapper personnelMapper;

    // ✅ Create Personnel
    public PersonnelDTO createPersonnel(PersonnelDTO dto) {
        log.info("Creating personnel");
        Personnel personnel = personnelMapper.toEntity(dto);
        Personnel saved = personnelRepository.save(personnel);
        log.info("Personnel created");
        return personnelMapper.toDto(saved);
    }

    // ✅ Get All Personnel
    public List<PersonnelDTO> getAllPersonnel() {
        log.info("Getting all personnel");
        return personnelRepository.findAll()
                .stream()
                .map(personnelMapper::toDto)
                .collect(Collectors.toList());
    }

    // ✅ Get by ID
    public PersonnelDTO getPersonnelById(Long id) {
        log.info("Getting personnel with id {}", id);
        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new PersonnelNotFoundException("Personnel not found with id " + id));
        return personnelMapper.toDto(personnel);
    }

    // ✅ Update
    public PersonnelDTO updatePersonnel(Long id, PersonnelDTO dto) {
        Personnel existing = personnelRepository.findById(id)
                .orElseThrow(() -> new PersonnelNotFoundException("Personnel not found with id " + id));

        personnelMapper.updateFromDto(dto, existing);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setPassword(dto.getPassword());
        }

        Personnel updated = personnelRepository.save(existing);
        return personnelMapper.toDto(updated);
    }

    // ✅ Delete
    public void deletePersonnel(Long id) {
        if (!personnelRepository.existsById(id)) {
            throw new PersonnelNotFoundException("Personnel not found with id " + id);
        }
        personnelRepository.deleteById(id);
    }

}
