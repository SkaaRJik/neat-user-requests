package ru.filippov.neat.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Log4j2
public class ExperimentData  {
    private Long neatConfigId;
    private Long projectId;
    private ProjectConfigDto.NormalizedDataDto normalizedData;
    private List<Map<String, Object>> neatSettings;
    private ProjectConfigDto.SelectedColumnsDto selectedColumns;
    private Integer trainIndexEnd;
    private Integer testIndexEnd;
    private Short predictionWindowSize;
    private Short predictionPeriod;


    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
