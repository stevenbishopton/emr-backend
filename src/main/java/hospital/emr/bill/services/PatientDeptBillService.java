package hospital.emr.bill.services;

import hospital.emr.bill.dtos.PatientDeptBillDto;
import hospital.emr.bill.entities.PatientDeptBill;
import hospital.emr.bill.mappers.PatientDeptBillMapper;
import hospital.emr.bill.repos.PatientDeptBillRepository;
import hospital.emr.notification.service.DepartmentNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientDeptBillService {

    private final PatientDeptBillRepository billRepository;
    private final PatientDeptBillMapper billMapper;
    private final DepartmentNotificationService departmentNotificationService;

    private String resolveCurrentUserDepartment() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getAuthorities() == null) {
                return null;
            }

            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority == null ? null : authority.getAuthority();
                if (role == null) continue;

                if (role.equalsIgnoreCase("ROLE_ADMIN") || role.equalsIgnoreCase("ADMIN")) return "admin";
                if (role.equalsIgnoreCase("ROLE_RECEPTIONIST") || role.equalsIgnoreCase("RECEPTIONIST")) return "reception";
                if (role.equalsIgnoreCase("ROLE_PHARMACIST") || role.equalsIgnoreCase("PHARMACIST")) return "pharmacy";
                if (role.equalsIgnoreCase("ROLE_DOCTOR") || role.equalsIgnoreCase("DOCTOR")) return "doctor";
                if (role.equalsIgnoreCase("ROLE_LAB_PERSONNEL") || role.equalsIgnoreCase("LAB_PERSONNEL")) return "laboratory";
                if (role.equalsIgnoreCase("ROLE_NURSE") || role.equalsIgnoreCase("NURSE")) return "nursing";
            }
        } catch (Exception ignored) {
            return null;
        }

        return null;
    }

    public PatientDeptBillDto createBill(PatientDeptBillDto dto) {
        PatientDeptBill bill = billMapper.toEntity(dto);
        PatientDeptBill savedBill = billRepository.save(bill);

        String toDepartment = dto.getIssuedTo();
        if (toDepartment != null && !toDepartment.isBlank()) {
            String fromDepartment = resolveCurrentUserDepartment();
            String metadata = String.format(
                    "{\"billId\":%d,\"visitId\":%s,\"patientId\":\"%s\",\"amount\":\"%s\",\"purpose\":\"%s\",\"issuer\":\"%s\",\"issuedTo\":\"%s\"}",
                    savedBill.getId(),
                    dto.getVisitId() == null ? "null" : dto.getVisitId().toString(),
                    dto.getPatientId(),
                    dto.getAmount(),
                    dto.getPurpose() == null ? "" : dto.getPurpose().replace("\"", "\\\""),
                    dto.getIssuer() == null ? "" : dto.getIssuer().replace("\"", "\\\""),
                    toDepartment
            );

            Set<String> notifyDepartments = new HashSet<>();
            notifyDepartments.add("admin");
            notifyDepartments.add(toDepartment.trim().toLowerCase());
            if (fromDepartment != null && !fromDepartment.isBlank()) {
                notifyDepartments.add(fromDepartment.trim().toLowerCase());
            }

            for (String dept : notifyDepartments) {
                if (dept == null || dept.isBlank()) continue;
                departmentNotificationService.sendDepartmentNotification(
                        fromDepartment,
                        dept,
                        "BILL_ISSUED",
                        "New Bill",
                        "A new bill is awaiting payment.",
                        "HIGH",
                        metadata
                );
            }
        }

        return billMapper.toDto(savedBill);
    }

    @Transactional(readOnly = true)
    public List<PatientDeptBillDto> getAllBills() {
        List<PatientDeptBill> bills = billRepository.findAll();
        return billMapper.toDtoList(bills);
    }

    @Transactional(readOnly = true)
    public List<PatientDeptBillDto> getAllAdmittedBills() {
        List<PatientDeptBill> bills = billRepository.findByIsAdmittedTrue();
        return billMapper.toDtoList(bills);
    }

    @Transactional(readOnly = true)
    public PatientDeptBillDto getBillById(Long id) {
        PatientDeptBill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + id));
        return billMapper.toDto(bill);
    }

    @Transactional(readOnly = true)
    public List<PatientDeptBillDto> getBillsByPatientId(String patientId) {
        List<PatientDeptBill> bills = billRepository.findByPatientId(patientId);
        return billMapper.toDtoList(bills);
    }

    @Transactional(readOnly = true)
    public List<PatientDeptBillDto> getBillsByPatientName(String patientName) {
        List<PatientDeptBill> bills = billRepository.findByPatientNamesContainingIgnoreCase(patientName);
        return billMapper.toDtoList(bills);
    }

    @Transactional(readOnly = true)
    public List<PatientDeptBillDto> getBillsByVisitId(Long visitId) {
        List<PatientDeptBill> bills = billRepository.findByVisitId(visitId);
        return billMapper.toDtoList(bills);
    }

    @Transactional(readOnly = true)
    public List<PatientDeptBillDto> getAdmittedBillsByVisitId(Long visitId) {
        List<PatientDeptBill> bills = billRepository.findByVisitIdAndIsAdmittedTrue(visitId);
        return billMapper.toDtoList(bills);
    }


    @Transactional(readOnly = true)
    public List<PatientDeptBillDto> getBillsByIssuer(String issuer) {
        List<PatientDeptBill> bills = billRepository.findByIssuer(issuer);
        return billMapper.toDtoList(bills);
    }

    public List<PatientDeptBillDto> getPaidBills() {
        List<PatientDeptBill> bills = billRepository.findByIsPaidTrue();
        return billMapper.toDtoList(bills);
    }



    public PatientDeptBillDto updateBill(Long id, PatientDeptBillDto dto) {
        PatientDeptBill existingBill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + id));

        boolean wasPaid = existingBill.getIsPaid() != null && existingBill.getIsPaid();

        billMapper.updateEntityFromDto(dto, existingBill);
        PatientDeptBill updatedBill = billRepository.save(existingBill);

        // Notify pharmacy if a pharmacy bill was just marked as paid by reception
        if (!wasPaid && updatedBill.getIsPaid() != null && updatedBill.getIsPaid()) {
            String fromDepartment = resolveCurrentUserDepartment();
            // Only notify if the payer is NOT pharmacy (to avoid self-notifications)
            if (fromDepartment != null && !fromDepartment.equals("pharmacy")) {
                String purpose = updatedBill.getPurpose() != null ? updatedBill.getPurpose().toLowerCase() : "";
                if (purpose.contains("pharmacy") || purpose.contains("drug") || purpose.contains("medication")) {
                    String metadata = String.format(
                            "{\"billId\":%d,\"patientId\":\"%s\",\"patientName\":\"%s\",\"amount\":\"%s\",\"paidBy\":\"%s\",\"paidAt\":\"%s\"}",
                            updatedBill.getId(),
                            updatedBill.getPatientId(),
                            updatedBill.getPatientNames() == null ? "" : updatedBill.getPatientNames().replace("\"", "\\\""),
                            updatedBill.getAmount(),
                            fromDepartment,
                            Instant.now().toString()
                    );

                    departmentNotificationService.sendDepartmentNotification(
                            fromDepartment,
                            "pharmacy",
                            "PHARMACY_BILL_PAID",
                            "Pharmacy Bill Paid",
                            String.format("Pharmacy bill #%d for %s was marked as PAID by %s.",
                                    updatedBill.getId(),
                                    updatedBill.getPatientNames(),
                                    fromDepartment),
                            "MEDIUM",
                            metadata
                    );
                }
            }
        }

        return billMapper.toDto(updatedBill);
    }

    public void deleteBill(Long id) {
        if (!billRepository.existsById(id)) {
            throw new RuntimeException("Bill not found with id: " + id);
        }
        billRepository.deleteById(id);
    }
}