package ru.filippov.neat.dto.services.preprocessing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NormalizationData {
    private Long experimentId;
    private String normalizationMethod;
    private boolean log;
    private String username;
    private String filePath;
    private String projectFolder;
}
