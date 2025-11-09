package hospital.emr.patient.services;

import hospital.emr.patient.dtos.PatientDTO;
import hospital.emr.patient.entities.MedicalHistory;
import hospital.emr.patient.entities.Patient;
import hospital.emr.patient.exceptions.PatientNotFoundException;
import hospital.emr.patient.mapper.PatientMapper;
import hospital.emr.patient.repos.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@AllArgsConstructor
@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    // --- CRUD METHODS ---
    @Transactional
    public PatientDTO createPatient(PatientDTO patientDTO){
        Patient patient = patientMapper.toEntity(patientDTO);

        // Generate and set the patient code
        String newPatientCode = generatePatientCodeOnce(); // Using the retry-on-error approach
        patient.setCode(newPatientCode);

        // Create a new MedicalHistory for the patient
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setPatient(patient); // Set the bidirectional relationship
        patient.setMedicalHistory(medicalHistory); // Set the bidirectional relationship

        // Save the patient entity. Due to cascade = CascadeType.ALL, MedicalHistory will also be saved.
        patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    @Transactional
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO){
        Patient patient = patientRepository.findById(id)
                .orElseThrow(PatientNotFoundException::new);
        patientMapper.updatePatientFromDto(patientDTO, patient);
        return patientMapper.toDto(patientRepository.save(patient));
    }

    @Transactional
    public void deletePatient(Long id){
        Patient patient = patientRepository.findById(id)
                .orElseThrow(PatientNotFoundException::new);
        patientRepository.deleteById(id);
    }

    // --- FIND METHODS ---

    @Transactional(readOnly = true)
    public Page<PatientDTO> getAllPatients(Pageable pageable) {
        Page<Patient> patients = patientRepository.findAll(pageable);
        return patients.map(patientMapper::toDto);
    }

    @Transactional(readOnly = true)
    public PatientDTO findPatientByPhoneNumber(String phoneNumber){
        Patient patient = patientRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(PatientNotFoundException::new);
        return patientMapper.toDto(patient);
    }

    @Transactional(readOnly = true)
    public PatientDTO findPatientByCode(String code){
        Patient patient = patientRepository.findByCode(code)
                .orElseThrow(PatientNotFoundException::new);
        return patientMapper.toDto(patient);
    }



    /**
     * Generates a unique patient code in the format "p{a-z}{randomNumberBetween0-9999}".
     * This method includes a basic uniqueness check by attempting to find an existing patient
     * with the generated code. In a high-concurrency environment, a more robust mechanism
     * (e.g., database-level unique constraint with retry logic) might be needed.
     *
     * @return A unique patient code.
     */
    private String generatePatientCodeOnce() {
        Random random = new Random();
        char randomLetter = (char) ('a' + random.nextInt(26)); // 'a' through 'z'
        int randomNumber = random.nextInt(10000); // 0 to 9999
        return String.format("p%c%04d", randomLetter, randomNumber);
    }
}