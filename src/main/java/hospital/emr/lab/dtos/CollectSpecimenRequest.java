package hospital.emr.lab.dtos;

import hospital.emr.lab.enums.SampleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectSpecimenRequest {

    private SampleType sampleType;

    private String accessionNumber;

    private List<Long> orderItemIds;

    private String notes;
}
