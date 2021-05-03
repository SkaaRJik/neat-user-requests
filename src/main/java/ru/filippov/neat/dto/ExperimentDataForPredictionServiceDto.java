package ru.filippov.neat.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Log4j2
@ToString
public class ExperimentDataForPredictionServiceDto {
    private Long experimentId;
    private Long projectId;
    private String username;
    private String projectFolder;
    private String dataFilename;
    private List<HashMap<String, Object>> columns;
    private Integer trainEndIndex;
    private Integer testEndIndex;
    private List<HashMap<String, Object>> neatSettings;
    private Short predictionWindowSize;
    private Short predictionPeriod;
}
