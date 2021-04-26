package ru.filippov.neat.dto.services.preprocessing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.filippov.neat.entity.ProjectStatus;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NormalizationResult {
    private Long projectId;
    private String normalizedDatasetFilename;
    private Map<String, Object> statistic;
    private ProjectStatus status;
}
