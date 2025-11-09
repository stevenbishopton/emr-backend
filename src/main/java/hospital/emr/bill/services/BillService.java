package hospital.emr.bill.services;

import hospital.emr.bill.dtos.BillDTO;
import hospital.emr.bill.entities.Bill;
import hospital.emr.bill.entities.SubBill;
import hospital.emr.bill.exceptions.BillNotFoundException;
import hospital.emr.bill.mappers.BillMapper;
import hospital.emr.bill.mappers.SubBillMapper;
import hospital.emr.bill.repos.BillRepository;
import hospital.emr.patient.entities.Patient;
import hospital.emr.patient.exceptions.PatientNotFoundException;
import hospital.emr.patient.repos.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final PatientRepository patientRepository;
    private final BillMapper billMapper;
    private final SubBillMapper subBillMapper;

    @Transactional
    public BillDTO createBill(BillDTO billDTO) {
        log.info("Creating bill for patientId={}", billDTO.getPatientId());

        // Fetch the patient entity
        Patient patient = patientRepository.findById(billDTO.getPatientId())
                .orElseThrow(() -> {
                    log.error("Patient with id={} not found when creating bill", billDTO.getPatientId());
                    return new PatientNotFoundException("Patient not found with id: " + billDTO.getPatientId());
                });

        // Convert DTO to entity
        Bill bill = billMapper.toEntity(billDTO);
        // Set the patient relationship
        bill.setPatient(patient);

        Bill saved = billRepository.save(bill);

        log.info("Bill created successfully with id={} for patientId={}", saved.getId(), billDTO.getPatientId());
        return billMapper.toDto(saved);
    }

    public BillDTO getBill(Long id) {
        log.debug("Fetching bill with id={}", id);
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Bill with id={} not found", id);
                    return new BillNotFoundException("Bill not found with id: " + id);
                });
        return billMapper.toDto(bill);
    }

    public List<BillDTO> getBillByPatientId(Long patientId) {
        log.debug("Fetching bill with patientId={}", patientId);
        List<Bill> bills = billRepository.findByPatient_Id(patientId);
        return billMapper.toDtoList(bills);
    }

    @Transactional
    public BillDTO updateBill(Long id, BillDTO billDTO) {
        log.info("Updating bill with id={}", id);
        Bill existing = billRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot update: bill with id={} not found", id);
                    return new BillNotFoundException("Bill not found with id: " + id);
                });

        // Handle patient update if patientId changed
        if (billDTO.getPatientId() != null && !billDTO.getPatientId().equals(existing.getPatient().getId())) {
            Patient patient = patientRepository.findById(billDTO.getPatientId())
                    .orElseThrow(() -> {
                        log.error("Patient with id={} not found when updating bill", billDTO.getPatientId());
                        return new PatientNotFoundException("Patient not found with id: " + billDTO.getPatientId());
                    });
            existing.setPatient(patient);
        }

        // Update other fields
        if (billDTO.getNote() != null) {
            existing.setNote(billDTO.getNote());
        }
        if (billDTO.getDateIssued() != null) {
            existing.setDateIssued(billDTO.getDateIssued());
        }
        if (billDTO.getSubBills() != null) {
            // Update subBills through mapper
            List<SubBill> updatedSubBills = subBillMapper.toEntityList(billDTO.getSubBills());
            existing.setSubBills(updatedSubBills);
        }

        Bill saved = billRepository.save(existing);
        log.info("Bill updated successfully with id={}", id);
        return billMapper.toDto(saved);
    }

    @Transactional
    public void deleteBill(Long id) {
        log.warn("Deleting bill with id={}", id);

        if (!billRepository.existsById(id)) {
            log.error("Cannot delete: bill with id={} not found", id);
            throw new BillNotFoundException("Bill not found with id: " + id);
        }
        billRepository.deleteById(id);
        log.info("Bill with id={} deleted successfully", id);
    }

    public Page<BillDTO> listBills(Pageable pageable) {
        log.debug("Listing bills with pagination page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return billRepository.findAll(pageable).map(billMapper::toDto);
    }
}