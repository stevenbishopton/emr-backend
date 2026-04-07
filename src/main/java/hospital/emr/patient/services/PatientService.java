package hospital.emr.patient.services;

import hospital.emr.patient.dtos.PatientDTO;
import hospital.emr.patient.entities.MedicalHistory;
import hospital.emr.patient.entities.Patient;
import hospital.emr.patient.exceptions.PatientNotFoundException;
import hospital.emr.patient.mapper.PatientMapper;
import hospital.emr.patient.repos.AdmissionRepository;
import hospital.emr.patient.repos.MedicalHistoryRepository;
import hospital.emr.patient.repos.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Random;

@AllArgsConstructor
@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AdmissionRepository admissionRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;

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
        
        // Delete admissions first to avoid foreign key constraint violations
        if (patient.getMedicalHistory() != null) {
            admissionRepository.deleteByMedicalHistory_Id(patient.getMedicalHistory().getId());
        }
        
        // Now delete the patient (cascade will delete medical history, visits, bills)
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


    @Transactional(readOnly = true)
    public PatientDTO findPatientById(Long patientId){
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(PatientNotFoundException::new);
        return patientMapper.toDto(patient);
    }

    /**
     * Find patient name by ID
     *
     * @param patientId The patient ID
     * @return Patient name or throws PatientNotFoundException
     */
    @Transactional(readOnly = true)
    public String findPatientNameById(Long patientId) {
        return patientRepository.findNameById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + patientId));
    }

    // --- SEARCH METHODS ---

    @Transactional(readOnly = true)
    public List<PatientDTO> searchPatientsByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return List.of();
        }
        String term = searchTerm.trim();
        // Use repository query to find all matching patients (convert Optional to List)
        Optional<Patient> opt = patientRepository.findByNamesContainingIgnoreCase(term);
        if (opt.isPresent()) {
            return List.of(patientMapper.toDto(opt.get()));
        }
        // Fallback: fetch all and filter
        Page<Patient> page = patientRepository.findAll(Pageable.unpaged());
        return page.stream()
                .filter(p -> p.getNames() != null && p.getNames().toLowerCase().contains(term.toLowerCase()))
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientDTO searchPatientByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new PatientNotFoundException("Patient code must be provided");
        }
        return patientRepository.findByCode(code.trim())
                .map(patientMapper::toDto)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with code: " + code));
    }

    @Transactional(readOnly = true)
    public PatientDTO searchPatientByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new PatientNotFoundException("Phone number must be provided");
        }
        return patientRepository.findByPhoneNumber(phoneNumber.trim())
                .map(patientMapper::toDto)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with phone: " + phoneNumber));
    }

    // --- HMO METHODS ---

    @Transactional(readOnly = true)
    public List<PatientDTO> findPatientsByHealthInsuranceStatus(Boolean isHealthInsured) {
        return patientRepository.findByIsHealthInsured(isHealthInsured)
                .stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientDTO findPatientByHmoPolicyNumber(String hmoPolicyNumber) {
        if (hmoPolicyNumber == null || hmoPolicyNumber.trim().isEmpty()) {
            throw new PatientNotFoundException("HMO policy number must be provided");
        }
        return patientRepository.findByHmoPolicyNumber(hmoPolicyNumber.trim())
                .map(patientMapper::toDto)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with HMO policy number: " + hmoPolicyNumber));
    }

    @Transactional(readOnly = true)
    public List<PatientDTO> searchPatientsByHmoName(String hmoName) {
        if (hmoName == null || hmoName.trim().isEmpty()) {
            return List.of();
        }
        return patientRepository.findByHmoNameContainingIgnoreCase(hmoName.trim())
                .stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PatientDTO> findPatientsWithHmo() {
        return patientRepository.findPatientsWithHmo()
                .stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean checkHmoPolicyNumberExists(String hmoPolicyNumber) {
        if (hmoPolicyNumber == null || hmoPolicyNumber.trim().isEmpty()) {
            return false;
        }
        return patientRepository.existsByHmoPolicyNumber(hmoPolicyNumber.trim());
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