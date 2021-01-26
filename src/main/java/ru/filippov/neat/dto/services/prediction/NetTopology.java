package ru.filippov.neat.dto.services.prediction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetTopology {
    private List<List<NeuronDto>> neuronsLayers;
    private List<NeuronConnection> connections;
}
