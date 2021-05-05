package ru.filippov.neat.dto.services.preprocessing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.filippov.neat.entity.ExperimentStatus;

import java.util.HashMap;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResult {
    private Long projectId;
    private Long experimentId;
    private Long experimentResultId;
    private HashMap<String, Object> predictionResult;
    private String predictionReportFile;
    private ExperimentStatus status;
}
