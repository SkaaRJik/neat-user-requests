package ru.filippov.neat.dto.services.prediction;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainResult {
    private double trainError;
    private double testError;
    private long timeSpent;

}
