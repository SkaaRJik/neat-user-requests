package ru.filippov.neat.dto.experiment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateExperimentDto {
    private String name;
    private String normalizationMethod;
    private Boolean enableLogTransform;
}
