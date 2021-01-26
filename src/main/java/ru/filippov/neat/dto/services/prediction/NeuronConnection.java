package ru.filippov.neat.dto.services.prediction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeuronConnection {

    private int fromId;
    private int toId;
    private double weight;
    private boolean enabled;


}
