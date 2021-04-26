package ru.filippov.neat.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectConfigDto implements Serializable {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColumnsDto  implements Serializable  {

        private List<Number> data;

        private String columnName;

        private String columnType;

        private Double minValue;

        private Double maxValue;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NormalizedDataDto implements Serializable {
        @NotNull
        private Map<String, Object> normalizationServiceData;

        @NotNull
        private List<ColumnsDto> columns;

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
