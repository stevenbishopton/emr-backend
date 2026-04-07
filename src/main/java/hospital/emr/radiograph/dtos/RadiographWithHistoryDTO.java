package hospital.emr.radiograph.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RadiographWithHistoryDTO extends RadiographDTO {
    private List<RadiographHistoryDTO> history;
    private RadiographCatalogDTO catalogInfo;
}
