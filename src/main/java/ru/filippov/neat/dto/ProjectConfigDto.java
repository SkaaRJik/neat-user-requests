package ru.filippov.neat.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
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
  
        Double minRange;
        Double maxRange;
        List<List<Double>> data;
        List<Double> mins;
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



    @NotNull
    private NormalizedDataDto normalizedData;

    @NotNull
    private List<Map<String, Object>> settings;

    @NotNull
    private DataIndexesDto dataIndexes;

    @NotNull
    private SelectedColumnsDto selectedColumns;

    @NotNull
    private Short windowSize;

    @NotNull
    private Short predictionPeriod;
}
