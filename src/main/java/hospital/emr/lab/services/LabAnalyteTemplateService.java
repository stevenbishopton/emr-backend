package hospital.emr.lab.services;

import hospital.emr.lab.dtos.LabAnalyteTemplateDTO;
import hospital.emr.lab.entities.LabAnalyteTemplate;
import hospital.emr.lab.repos.LabAnalyteTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabAnalyteTemplateService {

    private final LabAnalyteTemplateRepository repository;

    @Transactional(readOnly = true)
    public List<LabAnalyteTemplateDTO> getForTest(Long testId) {
        return repository.findByTestIdAndActiveTrueOrderByDisplayOrderAsc(testId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LabAnalyteTemplateDTO> getForTests(List<Long> testIds) {
        if (testIds == null || testIds.isEmpty()) {
            return List.of();
        }
        return repository.findByTestIdInOrderByDisplayOrderAsc(testIds).stream()
                .map(this::toDTO)
                .toList();
    }

    private LabAnalyteTemplateDTO toDTO(LabAnalyteTemplate template) {
        LabAnalyteTemplateDTO dto = new LabAnalyteTemplateDTO();
        dto.setId(template.getId());
        dto.setTestId(template.getTestId());
        dto.setAnalyteName(template.getAnalyteName());
        dto.setUnit(template.getUnit());
        dto.setRefLow(template.getRefLow());
        dto.setRefHigh(template.getRefHigh());
        dto.setCriticalLow(template.getCriticalLow());
        dto.setCriticalHigh(template.getCriticalHigh());
        dto.setDisplayOrder(template.getDisplayOrder());
        dto.setActive(template.getActive());
        return dto;
    }
}
