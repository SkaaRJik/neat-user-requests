package ru.filippov.neat.dto.services.prediction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.filippov.neat.entity.ExperimentStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExperimentNewStatusDto {
    private Long projectId;
    private Long experimentId;
    private ExperimentStatus status;
}
