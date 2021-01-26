package ru.filippov.neat.dto.services.prediction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WindowPredictionResult {

    long timeSpent;
    Double predictionError;
    List<Map<String, Object>> factorSigns;
    List<Map<String, Object>> targetSigns;
}
