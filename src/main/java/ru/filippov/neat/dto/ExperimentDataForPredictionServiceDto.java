package ru.filippov.neat.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
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
@ToString
public class ExperimentDataForPredictionServiceDto {
    private Long experimentId;
    private Long projectId;
    private String username;
    private String projectName;
    private String dataFilename;
    private String columns;
    private Integer trainEndIndex;
    private Integer testEndIndex;
    private String neatSettings;
    private Short predictionWindowSize;
    private Short predictionPeriod;
}
