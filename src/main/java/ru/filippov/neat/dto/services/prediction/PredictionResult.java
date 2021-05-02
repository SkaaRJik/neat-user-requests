package ru.filippov.neat.dto.services.prediction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.filippov.neat.entity.ProjectStatus;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PredictionResult {
    private Long experimentId;
    private String trainErrors;
    private String testErrors;
    private Double predictionError;
    private String predictionResultFile;
    private String windowTrainStatistic;
    private String model;
    private ProjectStatus status;
}
