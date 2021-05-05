package ru.filippov.neat.dto.services.preprocessing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.filippov.neat.entity.Experiment;
import ru.filippov.neat.entity.ExperimentResult;
import ru.filippov.neat.entity.Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportData {
    private Long experimentResultId;
    private HashMap<String, Object> model;
    private List<Double> trainErrors;
    private List<Double> testErrors;
    private Double predictionError;
    private String predictionResultFile;
    private HashMap<String, Object> windowTrainStatistic;
    private Long projectId;
    private String projectFolder;
    private String verifiedFile;
    private Long experimentId;
    private List<HashMap<String, Object>> columns;
    private Map<String, Object> legend;
    private List<HashMap<String, Object>> neatSettings;
    private String normalizationMethod;
    private Boolean enableLogTransform;
    private Short predictionPeriod;
    private Short predictionWindowSize;
    private Integer trainEndIndex;
    private Integer testEndIndex;
    private Map<String, Object> normalizationStatistic;


    public ReportData(Project project, Experiment experiment, ExperimentResult experimentResult) {
        this.experimentResultId = experimentResult.getId();
        this.model = experimentResult.getModel();
        this.trainErrors = experimentResult.getTrainErrors();
        this.testErrors = experimentResult.getTestErrors();
        this.predictionError = experimentResult.getPredictionError();
        this.predictionResultFile = experimentResult.getPredictionResultFile();
        this.windowTrainStatistic = experimentResult.getWindowTrainStatistic();
        this.projectId = project.getId();
        this.projectFolder = project.getProjectFolder();
        this.verifiedFile = project.getVerifiedFile();
        this.experimentId = experiment.getId();
        this.columns = experiment.getColumns();
        this.legend = project.getLegend();
        this.neatSettings = experiment.getNeatSettings();

        this.normalizationMethod = experiment.getNormalizationMethod();
        this.enableLogTransform = experiment.getEnableLogTransform();
        this.predictionPeriod = experiment.getPredictionPeriod();
        this.predictionWindowSize = experiment.getPredictionWindowSize();
        this.trainEndIndex = experiment.getTrainEndIndex();
        this.testEndIndex = experiment.getTestEndIndex();
        this.normalizationStatistic = experiment.getNormalizationStatistic();
    }
}
