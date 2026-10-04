package hospital.emr.lab.dtos;

import hospital.emr.lab.enums.TestStatus;

import java.time.LocalDateTime;

public class LabHistoryDTO {
    private Long id;
    private Long labTestRequestId;
    private Long labTestResultId;
    private TestStatus status;
    private String notes;
    private Long performedBy;
    private LocalDateTime timestamp;
    private String previousStatus;
    private String reason;
    private String performedByName;
    private String departmentName;

    public LabHistoryDTO() {
    }

    public LabHistoryDTO(Long id, Long labTestRequestId, Long labTestResultId, TestStatus status,
                         String notes, Long performedBy, LocalDateTime timestamp, String previousStatus,
                         String reason, String performedByName, String departmentName) {
        this.id = id;
        this.labTestRequestId = labTestRequestId;
        this.labTestResultId = labTestResultId;
        this.status = status;
        this.notes = notes;
        this.performedBy = performedBy;
        this.timestamp = timestamp;
        this.previousStatus = previousStatus;
        this.reason = reason;
        this.performedByName = performedByName;
        this.departmentName = departmentName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLabTestRequestId() {
        return labTestRequestId;
    }

    public void setLabTestRequestId(Long labTestRequestId) {
        this.labTestRequestId = labTestRequestId;
    }

    public Long getLabTestResultId() {
        return labTestResultId;
    }

    public void setLabTestResultId(Long labTestResultId) {
        this.labTestResultId = labTestResultId;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(Long performedBy) {
        this.performedBy = performedBy;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPerformedByName() {
        return performedByName;
    }

    public void setPerformedByName(String performedByName) {
        this.performedByName = performedByName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
