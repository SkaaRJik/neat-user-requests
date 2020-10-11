package ru.filippov.neat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectConfigDto implements Serializable {

    /*
     normalizationServiceData: {
          method: null
        },
        columns: [],
        trainEndIndex: null,
        testEndIndex: null,
        totalRows: null,
        inputs: 0,
        outputs: 0,
        }
        settings
        windowSize: 3,
        predictionPeriod: 10
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NormalizedDataDto implements Serializable {
        @NotNull
        private Map<String, Object> normalizationServiceData;

        @NotNull
        @NotEmpty
        private List<Map<String, Object>> columns;

        @NotNull
        private Integer trainEndIndex;

        @NotNull
        private Integer testEndIndex;

    }

    @NotNull
    private NormalizedDataDto normalizedData;

    @NotNull
    private List<Map<String, Object>> settings;

    @NotNull
    private Short windowSize;

    @NotNull
    private Short predictionPeriod;

}
