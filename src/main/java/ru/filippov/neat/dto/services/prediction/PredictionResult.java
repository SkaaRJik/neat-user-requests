package ru.filippov.neat.dto.services.prediction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.filippov.neat.entity.ExperimentStatus;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PredictionResult {
    private Long experimentId;
    private List<Double> trainErrors;
    private List<Double> testErrors;
    private Double predictionError;
    private String predictionResultFile;
    private HashMap<String, Object> windowTrainStatistic;
    private HashMap<String, Object> model;
    private ExperimentStatus status;
}
