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
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NormalizedDataDto implements Serializable {
        @NotNull
        Double minRange;

        @NotNull
        Double maxRange;

        @NotNull
        @NotEmpty
        List<List<Double>> data;

        @NotNull
        @NotEmpty
        List<Double> mins;

        @NotNull
        @NotEmpty
        List<Double> maxs;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataIndexesDto implements Serializable {
        @NotNull
        Integer trainEndIndex;

        @NotNull
        Integer testEndIndex;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectedColumnsDto implements Serializable {
        @NotNull
        Integer inputs;

        @NotNull
        Integer outputs;

        @NotNull
        @NotEmpty
        List<HashMap<String, String>> headers;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PredictionParamsDto implements Serializable {
        @NotNull
        Short windowSize;

        @NotNull
        Short predictionPeriod;
    }

    @NotNull
    private NormalizedDataDto normalizedData;

    @NotNull
    private List<Map<String, Object>> settings;

    @NotNull
    private DataIndexesDto dataIndexes;

    @NotNull
    private SelectedColumnsDto selectedColumns;

    @NotNull
    private PredictionParamsDto predictionParams;




}
