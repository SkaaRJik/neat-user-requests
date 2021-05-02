package ru.filippov.neat.dto.services.prediction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.filippov.neat.entity.ProjectStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExperimentStatusDto {
    Long projectId;
    ProjectStatus status;
}
