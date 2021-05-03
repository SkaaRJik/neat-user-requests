package ru.filippov.neat.dto.services.preprocessing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.filippov.neat.entity.ProjectStatus;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NormalizationResult {
    private Long experimentId;
    private String normalizedDatasetFilename;
    private HashMap<String, Object> statistic;
    private ProjectStatus status;
}
