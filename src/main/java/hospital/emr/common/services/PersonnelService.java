package hospital.emr.common.services;

import hospital.emr.common.dtos.PersonnelDTO;
import hospital.emr.common.entities.*;
import hospital.emr.common.enums.PersonnelType;
import hospital.emr.common.exceptions.PersonnelNotFoundException;
import hospital.emr.common.mappers.PersonnelMapper;
import hospital.emr.common.repos.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final PersonnelMapper personnelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    // ✅ Create Personnel
    public PersonnelDTO createPersonnel(PersonnelDTO dto) {
        log.info("Creating personnel");
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required when creating personnel");
        }
        Personnel personnel = personnelMapper.toEntity(dto);
        personnel.setPassword(passwordEncoder.encode(dto.getPassword()));
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
    @Transactional
    public PersonnelDTO updatePersonnel(Long id, PersonnelDTO dto) {
        Personnel existing = personnelRepository.findById(id)
                .orElseThrow(() -> new PersonnelNotFoundException("Personnel not found with id " + id));

        // Check if personnel type is changing
        PersonnelType existingType = personnelMapper.toDto(existing).getPersonnelType();
        PersonnelType newType = dto.getPersonnelType();

        if (newType != null && existingType != newType) {
            log.info("Personnel type changing from {} to {}, updating discriminator", existingType, newType);

            // Update the shared columns on the existing row first (keeps the same id
            // and any foreign keys pointing at this personnel record).
            personnelMapper.updateFromDto(dto, existing);
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                existing.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            personnelRepository.save(existing);
            personnelRepository.flush();

            // Flip the SINGLE_TABLE discriminator so the row is read back as the new subtype.
            entityManager
                    .createNativeQuery("UPDATE personnel SET personnel_type = :type WHERE id = :id")
                    .setParameter("type", newType.name())
                    .setParameter("id", id)
                    .executeUpdate();

            entityManager.clear();

            Personnel reloaded = personnelRepository.findById(id)
                    .orElseThrow(() -> new PersonnelNotFoundException("Personnel not found with id " + id));
            return personnelMapper.toDto(reloaded);
        }

        // Normal update - same type
        personnelMapper.updateFromDto(dto, existing);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
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
