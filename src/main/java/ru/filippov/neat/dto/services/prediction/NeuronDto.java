package ru.filippov.neat.dto.services.prediction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeuronDto {
    private double lastActivation;
    private double bias;
    private double[] weights;
    private String activationFunction;
    private int id;
    private String type;
    private String label;

}
