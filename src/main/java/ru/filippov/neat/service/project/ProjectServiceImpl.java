package ru.filippov.neat.service.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.filippov.neat.controller.ServerSentEventController;
import ru.filippov.neat.dto.services.prediction.ExperimentData;
import ru.filippov.neat.dto.services.prediction.ExperimentNewStatusDto;
import ru.filippov.neat.dto.services.prediction.PredictionResult;
import ru.filippov.neat.dto.services.preprocessing.*;
import ru.filippov.neat.entity.*;
import ru.filippov.neat.exception.PermissionException;
import ru.filippov.neat.exception.ResourceNotFoundException;
import ru.filippov.neat.exception.project.ProjectException;
import ru.filippov.neat.rabbitmq.RabbitMQWriter;
import ru.filippov.neat.repository.ExperimentRepository;
import ru.filippov.neat.repository.ExperimentResultsRepository;
import ru.filippov.neat.repository.ProjectRepository;
import ru.filippov.neat.samba.SambaWorker;
import ru.filippov.utils.Translitirator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl {

    private final ExperimentResultsRepository experimentResultsRepository;

    private final ProjectRepository projectRepository;

    @Value("${path.projects.data:./public/projects}")
    private String projectsLocation;

    @Value("${app.project.restrictions.max_allowed_experiments:10}")
    private int MAX_ALLOWED_EXPERIMENTS;

    @Value("${app.project.restrictions.max_allowed_projects:50}")
    private int MAX_ALLOWED_PROJECTS;

    private final ServerSentEventController serverSentEventController;

    private final RabbitMQWriter rabbitMQWriter;

    private final ExperimentRepository experimentRepository;

    private final SambaWorker sambaWorker;

    public ProjectServiceImpl(ExperimentResultsRepository experimentResultsRepository, ProjectRepository projectRepository, ExperimentRepository experimentRepository, RabbitMQWriter rabbitMQWriter, SambaWorker sambaWorker, ServerSentEventController serverSentEventController) {
        this.experimentResultsRepository = experimentResultsRepository;
        this.projectRepository = projectRepository;
        this.experimentRepository = experimentRepository;
        this.rabbitMQWriter = rabbitMQWriter;
        this.sambaWorker = sambaWorker;
        this.serverSentEventController = serverSentEventController;
    }


    public Project createProject(User user, String projectName, MultipartFile sourceFile) throws IOException, ResourceNotFoundException, ProjectException {

        if (projectRepository.findAllByUserId(user.getId(), PageRequest.of(1, 1, Sort.unsorted())).getTotalElements() >= MAX_ALLOWED_PROJECTS) {
            throw new ResourceNotFoundException("ERROR_CREATED_MAX_PROJECTS");
        }

        Project project = projectRepository.findOneByNameAndUserId(projectName, user.getId()).orElse(null);

        if (project != null) {
            throw new ProjectException("ERROR_PROJECT_ALREADY_EXISTS");
        }

        String transliteratedProjectName = Translitirator.cyr2lat(projectName).replaceAll("[^\\d\\w А-Яа-я]", "").replaceAll(" ", "_");

        project = new Project(
                null,
                projectName,
                user,
                ProjectStatus.NEW,
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                String.format("%s/%s", user.getUsername(), transliteratedProjectName),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        String sourceFilePath = this.sambaWorker.writeSourceFile(sourceFile, project.getProjectFolder());

        project.setSourceFile(sourceFilePath);
        project.setStatus(ProjectStatus.VERIFICATION);
        projectRepository.save(project);

        rabbitMQWriter.sendDataToVerify(
                new VerificationData(
                        project.getId(),
                        user.getUsername(),
                        project.getSourceFile(),
                        project.getProjectFolder()
                )
        );

        return project;
    }

    public Project attachSourceFileToProject(User user, Long projectId, MultipartFile multipartFile) throws ResourceNotFoundException, PermissionException, IOException, ProjectException {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if (project.getUser().getId() != user.getId()) {
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        if (project.getStatus() == ProjectStatus.VERIFICATION) {
            throw new ProjectException("VERIFICATION_IN_PROGRESS");
        }


        String sourceFile = this.sambaWorker.writeSourceFile(multipartFile, project.getProjectFolder());

        project.setSourceFile(sourceFile);
        project.setStatus(ProjectStatus.VERIFICATION);
        projectRepository.save(project);

        try {
            rabbitMQWriter.sendDataToVerify(
                    new VerificationData(
                            project.getId(),
                            user.getUsername(),
                            project.getSourceFile(),
                            project.getProjectFolder()
                    )
            );
        } catch (Exception exception) {
            project.setStatus(ProjectStatus.SENDING_TO_VERIFICATION_SERVICE_ERROR);
            projectRepository.save(project);
            throw exception;
        }


        return project;
    }

    public Page<Project> getProjectsByUser(User user, int curPage, int itemsPerPage) {
        Pageable pageable = PageRequest.of(curPage - 1, itemsPerPage);

        Page<Project> projects = projectRepository.findAllByUserId(user.getId(), pageable);

        return projects;
    }

    public Project getProjectById(Long id, User user) throws PermissionException, ResourceNotFoundException {

        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if (project.getUser().getId() != user.getId()) {
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        return project;
    }


    public Experiment normalizeData(User user, Long projectId, Long experimentId, String method, boolean enableLogTransform) throws PermissionException, ResourceNotFoundException, IOException, ProjectException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if (project.getUser().getId() != user.getId()) {
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        if (project.getVerifiedFile() == null) {
            throw new ProjectException("ERROR_VERIFIED_FILE_NOT_EXIST");
        }

        Experiment experiment = experimentRepository.findById(experimentId).orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));

        if (experiment.getStatus() == ExperimentStatus.NORMALIZATION) {
            throw new ProjectException("NORMALIZATION_PROCESS_ALREADY_RAN");
        }

        if (experiment.getStatus() == ExperimentStatus.CREATING_REPORT) {
            throw new ProjectException("REPORT_PROCESS_ALREADY_RAN");
        }

        if (experiment.getStatus() == ExperimentStatus.IN_QUEUE) {
            throw new ProjectException("PREDICTION_PROCESS_ALREADY_RAN");
        }

        if (experiment.getStatus() == ExperimentStatus.PREDICTION) {
            throw new ProjectException("PREDICTION_PROCESS_ALREADY_RAN");
        }


        project.setUpdatedDate(LocalDateTime.now());
        project.setLastActiveExperiment(experiment);
        experiment.setStatus(ExperimentStatus.NORMALIZATION);
        experiment.setNormalizationMethod(method);
        experiment.setEnableLogTransform(enableLogTransform);
        projectRepository.save(project);
        experimentRepository.save(experiment);
        try {
            rabbitMQWriter.sendDataToNormalize(new NormalizationData(
                            experimentId,
                            method,
                            enableLogTransform,
                            user.getUsername(),
                            project.getVerifiedFile(),
                            project.getProjectFolder()
                    )
            );
        } catch (Exception exception) {
            experiment.setStatus(ExperimentStatus.SENDING_TO_NORMALIZATION_SERVICE_ERROR);
            experimentRepository.save(experiment);
            throw exception;
        }

        return experiment;

    }


    public Experiment createExperiment(User user, Long projectId) throws PermissionException, ResourceNotFoundException, ProjectException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));
        String experimentName = String.format("Experiment_%d", System.nanoTime());
        if (project.getUser().getId() != user.getId()) {
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }
        Experiment experiment = null;
        List<Experiment> experiments = project.getExperiments();
        if (experiments != null) {
            if (experiments.size() < MAX_ALLOWED_EXPERIMENTS) {
                experiment = new Experiment(
                        null,
                        experimentName,
                        project,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        null,
                        ExperimentStatus.NEW_EXPERIMENT
                );
                experiment = experimentRepository.save(experiment);
                experiments.add(experiment);
                project.setLastActiveExperiment(experiment);
                projectRepository.save(project);
            } else {
                throw new ProjectException("ERROR_MAX_EXPERIMENTS_REACHED");
            }
        } else {
            experiments = new ArrayList<>(1);
            experiment = new Experiment(
                    null,
                    experimentName,
                    project,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    null,
                    ExperimentStatus.NEW_EXPERIMENT
            );
            experiment = experimentRepository.save(experiment);
            experiments.add(experiment);
            project.setLastActiveExperiment(experiment);
            project.setExperiments(experiments);
            projectRepository.save(project);
        }
        return experiment;
    }


    public Experiment updateExperiment(User user, Long projectId, Long experimentId, String experimentName) throws PermissionException, ResourceNotFoundException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if (project.getUser().getId() != user.getId()) {
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        Experiment experiment = experimentRepository.findById(experimentId).orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));
        project.setUpdatedDate(LocalDateTime.now());
        experiment.setUpdatedDate(LocalDateTime.now());
        experiment.setName(experimentName);

        experiment = experimentRepository.save(experiment);
        projectRepository.save(project);
        return experiment;
    }

    public Experiment updateExperimentStatus(ExperimentNewStatusDto newStatusDto) throws ResourceNotFoundException {
        Experiment experiment = experimentRepository.findById(newStatusDto.getExperimentId()).orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));

        experiment.setStatus(newStatusDto.getStatus());
        experiment.setUpdatedDate(LocalDateTime.now());

        return experimentRepository.save(experiment);
    }

    public Experiment getExperimentById(User user, Long projectId, Long experimentId) throws ResourceNotFoundException, PermissionException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if (project.getUser().getId() != user.getId()) {
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        Experiment experiment = experimentRepository.findById(experimentId).orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));
        return experiment;
    }

    public void setVerificationResult(VerificationResult verificationResult) throws ResourceNotFoundException {
        Project project = this.projectRepository.findById(verificationResult.getProjectId()).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));
        project.setUpdatedDate(LocalDateTime.now());
        project.setStatus(verificationResult.getStatus());
        project.setVerificationErrors(verificationResult.getErrors());
        project.setHeaders(verificationResult.getHeaders());
        project.setVerificationInfo(verificationResult.getInfo());
        project.setLegend(verificationResult.getLegend());
        project.setVerifiedFile(verificationResult.getVerifiedFile());
        project.setLogIsAllowed(verificationResult.isLogIsAllowed());
        project.setRows(verificationResult.getRows());
        this.projectRepository.save(project);
    }

    public void setNormalizationResult(NormalizationResult normalizationResult) throws ResourceNotFoundException, JsonProcessingException {

        Experiment experiment = experimentRepository
                .findById(normalizationResult.getExperimentId())
                .orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));

        //Project project = projectRepository.findById(experiment.getProject().getId()).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));
        Project project = experiment.getProject();
        project.setLastActiveExperiment(experiment);
        project.setUpdatedDate(LocalDateTime.now());

        experiment.setStatus(normalizationResult.getStatus());
        experiment.setUpdatedDate(LocalDateTime.now());
        experiment.setNormalizationStatistic(normalizationResult.getStatistic());
        experiment.setNormalizedDataFile(normalizationResult.getNormalizedDatasetFilename());

        this.projectRepository.save(project);
        this.experimentRepository.save(experiment);

        this.serverSentEventController.updateExperiment(project.getUser().getId(), experiment);
    }

    public Experiment startPrediction(User user, Long projectId, Long experimentId, ExperimentData experimentData) throws PermissionException, ResourceNotFoundException, JsonProcessingException, ProjectException {

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if (project.getUser().getId() != user.getId()) {
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        Experiment experiment = experimentRepository.findById(experimentId).orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));

        if (experiment.getNormalizedDataFile() == null) {
            throw new ProjectException("ERROR_NORMALIZATION_FILE_NOT_FOUND");
        }

        if (experiment.getStatus() == ExperimentStatus.NORMALIZATION) {
            throw new ProjectException("NORMALIZATION_PROCESS_ALREADY_RAN");
        }

        if (experiment.getStatus() == ExperimentStatus.CREATING_REPORT) {
            throw new ProjectException("REPORT_PROCESS_ALREADY_RAN");
        }

        if (experiment.getStatus() == ExperimentStatus.IN_QUEUE) {
            throw new ProjectException("PREDICTION_PROCESS_ALREADY_RAN");
        }

        if (experiment.getStatus() == ExperimentStatus.PREDICTION) {
            throw new ProjectException("PREDICTION_PROCESS_ALREADY_RAN");
        }

        project.setLastActiveExperiment(experiment);
        project.setUpdatedDate(LocalDateTime.now());

        experiment.setUpdatedDate(LocalDateTime.now());
        experiment.setStatus(ExperimentStatus.IN_QUEUE);
        experiment.setColumns(experimentData.getColumns());
        experiment.setTrainEndIndex(experimentData.getTrainEndIndex());
        experiment.setTestEndIndex(experimentData.getTestEndIndex());
        experiment.setNeatSettings(experimentData.getNeatSettings());
        experiment.setPredictionPeriod(experimentData.getPredictionPeriod());
        experiment.setPredictionWindowSize(experimentData.getPredictionWindowSize());
        experiment.setExperimentResult(null);

        ExperimentData sendingExperimentConfig = new ExperimentData(
                experimentId,
                projectId,
                user.getUsername(),
                project.getProjectFolder(),
                experiment.getNormalizedDataFile(),
                experimentData.getColumns(),
                experiment.getTrainEndIndex(),
                experiment.getTestEndIndex(),
                experimentData.getNeatSettings(),
                experiment.getPredictionWindowSize(),
                experiment.getPredictionPeriod()
        );

        experimentRepository.save(experiment);
        projectRepository.save(project);
        try {
            rabbitMQWriter.sendDataToPredict(sendingExperimentConfig);
        } catch (Exception ex) {
            experiment.setStatus(ExperimentStatus.SENDING_TO_PREDICTION_SERVICE_ERROR);
            experimentRepository.save(experiment);
            throw ex;
        }
        return experiment;
    }

    public void setPredictionResult(PredictionResult predictionResult) throws ResourceNotFoundException, JsonProcessingException {
        Experiment experiment = experimentRepository
                .findById(predictionResult.getExperimentId())
                .orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));


        Project project = experiment.getProject();
        project.setUpdatedDate(LocalDateTime.now());


        ExperimentResult experimentResult = new ExperimentResult(
                experiment.getId(),
                predictionResult.getModel(),
                predictionResult.getTrainErrors(),
                predictionResult.getTestErrors(),
                predictionResult.getPredictionError(),
                null,
                LocalDateTime.now(),
                predictionResult.getPredictionResultFile(),
                null,
                predictionResult.getWindowTrainStatistic()
        );

        experiment.setUpdatedDate(LocalDateTime.now());
        experiment.setStatus(ExperimentStatus.PREDICTED);
        experiment.setExperimentResult(experimentResult);

        this.experimentResultsRepository.save(experimentResult);
        this.experimentRepository.save(experiment);
        this.projectRepository.save(project);

        if (experiment.getStatus() == ExperimentStatus.PREDICTED) {
            try {
                this.rabbitMQWriter.sendDataToReportService(
                        new ReportData(project, experiment, experimentResult)
                );
            } catch (Exception ex) {
                experiment.setStatus(ExperimentStatus.SENDING_TO_REPORT_SERVICE_ERROR);
                experimentRepository.save(experiment);
                throw ex;
            }
        }

    }

    public void setReportResult(ReportResult reportResult) throws ResourceNotFoundException {

        Experiment experiment = experimentRepository
                .findById(reportResult.getExperimentId())
                .orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));

        Project project = experiment.getProject();
        project.setLastActiveExperiment(experiment);
        project.setUpdatedDate(LocalDateTime.now());

        ExperimentResult experimentResult = experiment.getExperimentResult();
        experimentResult.setPredictionReportFile(reportResult.getPredictionReportFile());
        experimentResult.setPredictionResult(reportResult.getPredictionResult());

        experiment.setUpdatedDate(LocalDateTime.now());

        this.experimentResultsRepository.save(experimentResult);
        experiment = this.experimentRepository.save(experiment);
        this.projectRepository.save(project);

        this.serverSentEventController.updateExperiment(project.getUser().getId(), experiment);

    }

    public Experiment copyExperiment(User user, Long projectId, Long experimentId, Long rewriteExperimentId) throws PermissionException, ResourceNotFoundException, ProjectException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));
        if (project.getUser().getId() != user.getId()) {
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        List<Experiment> experiments = project.getExperiments();

        Experiment sourceExperiment = experimentRepository.findById(experimentId)
                .orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));
        Experiment newExperiment = null;


        if (experiments.size() < MAX_ALLOWED_EXPERIMENTS || rewriteExperimentId != null) {
            newExperiment = sourceExperiment.copy(rewriteExperimentId);
            newExperiment = experimentRepository.save(newExperiment);
            experiments.add(newExperiment);
            project.setLastActiveExperiment(newExperiment);
            projectRepository.save(project);
        } else {
            throw new ProjectException("ERROR_MAX_EXPERIMENTS_REACHED");
        }
        return newExperiment;
    }

    public byte[] getExperimentReport(User user, Long projectId, Long experimentId) throws PermissionException, ProjectException, ResourceNotFoundException, IOException {

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if (project.getUser().getId() != user.getId()) {
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        Experiment experiment = experimentRepository.findById(experimentId).orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));

        if (experiment.getExperimentResult() == null) {
            throw new ProjectException("ERROR_EXPERIMENT_RESULT_DOES_NOT_EXIST");
        }

        if(experiment.getExperimentResult().getPredictionReportFile() == null) {
            throw new ProjectException("ERROR_REPORT_FILE_DOES_NOT_EXIST");
        }

        return this.sambaWorker.readFile(experiment.getExperimentResult().getPredictionReportFile());


    }


    /*public boolean saveNeatConfig(Long projectId, Long userId, ProjectConfigDto params, Long configId) throws ResourceNotFoundException, PermissionException, NeatConfigurationNotFoundException, JsonProcessingException {

        final Project project = projectRepository
                .findById(projectId)
                .orElseThrow(ResourceNotFoundException::new);

        if(project.getUser().getId() != userId) {
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        Experiment experiment = null;

        if(configId != null) {
            experiment = neatConfigRepository
                    .findById(configId)
                    .orElseThrow(NeatConfigurationNotFoundException::new);

            experiment = updateNeatConfig(params, experiment);
        } else {
            final List<Experiment> experiments = project.getExperiments();

            if(experiments.size() >= MAX_ALLOWED_EXPERIMENTS) {
                experiments.sort(Comparator.comparing(Experiment::getCreationDate).reversed());
                experiment = experiments.get(0);
                experiment = updateNeatConfig(params, experiment);
            } else {
                experiment = new Experiment(
                        null,
                        params.getNormalizedData(),
                        params.getSettings(),
                        params.getWindowSize(),
                        params.getPredictionPeriod(),
                        LocalDateTime.now(),
                        project,
                        null,
                        null,
                        null
                );
                experiment = neatConfigRepository.save(experiment);
            }
        }

        //rabbitMQWriter.writeIntoExperimentServerQueue(neatConfig.toExperimentData());

        project.setStatus(ProjectStatus.IN_QUEUE);
        projectRepository.save(project);

        return true;
    }*/
//
//    private Experiment updateNeatConfig(ProjectConfigDto params, Experiment experiment) {
//        experiment.setCreationDate(LocalDateTime.now());
//        experiment.setNormalizedData(params.getNormalizedData());
//        experiment.setNeatSettings(params.getSettings());
//        experiment.setPredictionWindowSize(params.getWindowSize());
//        experiment.setPredictionPeriod(params.getPredictionPeriod());
//
//        return neatConfigRepository.save(experiment);
//    }
//
//    public void updateProjectStatus(Long projectId, ProjectStatus projectStatus) throws ResourceNotFoundException {
//        Project project = this.projectRepository
//                .findById(projectId)
//                .orElseThrow(() -> new ResourceNotFoundException(String.format("Project with [id] = %d not found!", projectId)));
//
//        project.setStatus(projectStatus);
//
//        this.projectRepository.save(project);
//
//
//    }

}
