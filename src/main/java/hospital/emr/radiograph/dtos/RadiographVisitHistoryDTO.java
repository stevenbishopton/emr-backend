package hospital.emr.radiograph.dtos;

import hospital.emr.radiograph.enums.RadiographStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RadiographVisitHistoryDTO {

    private Long id;
    
    private PatientDTO patient;
    
    private VisitDTO visit;
    
    private PersonnelDTO requestedBy;
    
    private PersonnelDTO performedBy;
    
    private PersonnelDTO radiologist;
    
    private RadiographStatus status;
    
    private String visitType;
    
    private String clinicalNotes;
    
    private String radiologistReport;
    
    private String technicianNotes;
    
    private String reportUrl;
    
    private String imageUrl;
    
    private LocalDateTime visitDate;
    
    private LocalDateTime completedDate;
    
    private LocalDateTime scheduledDate;
    
    private List<RadiographVisitTestDTO> testsPerformed;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Long version;

    // Nested DTOs for patient, visit, and personnel
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private String fullName;
        private String dateOfBirth;
        private String sex;
        private String phoneNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VisitDTO {
        private Long id;
        private LocalDateTime visitDate;
        private String visitType;
        private String complaint;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonnelDTO {
        private Long id;
        private String names;
        private String personnelType;
        private String department;
    }
}
