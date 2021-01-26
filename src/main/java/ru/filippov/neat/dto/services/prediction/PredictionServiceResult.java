package ru.filippov.neat.dto.services.prediction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PredictionServiceResult {
    private Long configId;
    private TrainResult train;
    private WindowPredictionResult prediction;
    private NetTopology model;
}
