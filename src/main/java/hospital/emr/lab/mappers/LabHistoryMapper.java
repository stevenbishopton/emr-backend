package hospital.emr.lab.mappers;

import hospital.emr.lab.dtos.LabHistoryDTO;
import hospital.emr.lab.entities.LabHistory;

public class LabHistoryMapper {

    public static LabHistoryDTO toDTO(LabHistory entity) {
        if (entity == null) {
            return null;
        }
        return new LabHistoryDTO(
            entity.getId(),
            entity.getLabTestRequestId(),
            entity.getLabTestResultId(),
            entity.getStatus(),
            entity.getNotes(),
            entity.getPerformedBy(),
            entity.getTimestamp(),
            entity.getPreviousStatus(),
            entity.getReason(),
            entity.getPerformedByName(),
            entity.getDepartmentName()
        );
    }

    public static LabHistory toEntity(LabHistoryDTO dto) {
        if (dto == null) {
            return null;
        }
        LabHistory entity = new LabHistory();
        entity.setId(dto.getId());
        entity.setLabTestRequestId(dto.getLabTestRequestId());
        entity.setLabTestResultId(dto.getLabTestResultId());
        entity.setStatus(dto.getStatus());
        entity.setNotes(dto.getNotes());
        entity.setPerformedBy(dto.getPerformedBy());
        entity.setPreviousStatus(dto.getPreviousStatus());
        entity.setReason(dto.getReason());
        entity.setPerformedByName(dto.getPerformedByName());
        entity.setDepartmentName(dto.getDepartmentName());
        return entity;
    }
}
