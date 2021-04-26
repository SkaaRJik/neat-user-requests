package ru.filippov.neat.service.project;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.filippov.neat.dto.services.preprocessing.NormalizationData;
import ru.filippov.neat.dto.services.preprocessing.VerificationData;
import ru.filippov.neat.entity.Experiment;
import ru.filippov.neat.entity.Project;
import ru.filippov.neat.entity.ProjectStatus;
import ru.filippov.neat.entity.User;
import ru.filippov.neat.exception.PermissionException;
import ru.filippov.neat.exception.ResourceNotFoundException;
import ru.filippov.neat.exception.project.ProjectException;
import ru.filippov.neat.rabbitmq.RabbitMQWriter;
import ru.filippov.neat.repository.ExperimentRepository;
import ru.filippov.neat.repository.ProjectRepository;
import ru.filippov.neat.samba.SambaWorker;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl {
    private final ProjectRepository projectRepository;

    @Value("${path.projects.data:./public/projects}")
    private String projectsLocation;

    @Value("${app.project.restrictions.max_allowed_experiments:10}")
    private int MAX_ALLOWED_EXPERIMENTS;

    @Value("${app.project.restrictions.max_allowed_projects:50}")
    private int MAX_ALLOWED_PROJECTS;

    private final RabbitMQWriter rabbitMQWriter;

    private final ExperimentRepository experimentRepository;

    private final SambaWorker sambaWorker;

    public ProjectServiceImpl(ProjectRepository projectRepository, ExperimentRepository experimentRepository, RabbitMQWriter rabbitMQWriter, SambaWorker sambaWorker) {
        this.projectRepository = projectRepository;
        this.experimentRepository = experimentRepository;
        this.rabbitMQWriter = rabbitMQWriter;
        this.sambaWorker = sambaWorker;
    }



    public Project createProject(User user, String projectName) throws IOException, ResourceNotFoundException, ProjectException {

        if(projectRepository.findAllByUserId(user.getId(), PageRequest.of(1, 1, Sort.unsorted())).getTotalElements() >= MAX_ALLOWED_PROJECTS){
            throw new ResourceNotFoundException("ERROR_CREATED_MAX_PROJECTS");
        }

        Project project = projectRepository.findOneByNameAndUserId(projectName, user.getId()).orElse( null);
        if(project != null) {
            throw new ProjectException("ERROR_PROJECT_ALREADY_EXISTS");
        }

        project = projectRepository.save(new Project(
                null,
                projectName,
                user,
                ProjectStatus.NEW,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        ));


        return project;
    }

    public void attachSourceFileToProject(User user, Long projectId, MultipartFile multipartFile) throws ResourceNotFoundException, PermissionException, IOException, ProjectException {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow( () -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if(project.getUser().getId() != user.getId()){
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        if(!(project.getStatus() == ProjectStatus.VERIFICATION ||
                project.getStatus() == ProjectStatus.VERIFICATION_ERROR ||
                project.getStatus() == ProjectStatus.VERIFICATION_SERVICE_ERROR
        )){
            throw new ProjectException("SOURCE_FILE_ALREADY_VERIFIED");
        }

        String sourceFile = this.sambaWorker.writeSourceFile(multipartFile, project.getName(), user.getUsername());

        project.setSourceFile(sourceFile);
        project.setStatus(ProjectStatus.VERIFICATION);
        projectRepository.save(project);

        rabbitMQWriter.sendDataToVerify(
                new VerificationData(
                        project.getId(),
                        user.getUsername(),
                        sourceFile
                )
        );
    }

    public Page<Project> getProjectsByUser(User user, int curPage, int itemsPerPage) {
        Pageable pageable = PageRequest.of(curPage-1, itemsPerPage);

        Page<Project> projects = projectRepository.findAllByUserId(user.getId(), pageable);

        return projects;
    }

    public Project getProjectById(Long id, User user) throws PermissionException, ResourceNotFoundException {

        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if(project.getUser().getId() != user.getId()){
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        return project;
    }

    public void normalizeData(User user, Long projectId, Long experimentId, String method, boolean enableLogTransform) throws PermissionException, ResourceNotFoundException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));
        Experiment experiment = experimentRepository.findById(experimentId).orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));

        if(project.getUser().getId() != user.getId()){
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }
        project.setStatus(ProjectStatus.NORMALIZATION);
        project.setUpdatedDate(LocalDateTime.now());
        experiment.setNormalizationMethod(method);

        projectRepository.save(project);
        experimentRepository.save(experiment);

        rabbitMQWriter.sendDataToNormalize(new NormalizationData(experimentId,
                method,
                enableLogTransform,
                user.getUsername(),
                project.getSourceFile())
        );
    }

    public Experiment createExperiment(User user, Long projectId, String experimentName) throws PermissionException, ResourceNotFoundException, ProjectException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if(project.getUser().getId() != user.getId()){
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }
        Experiment experiment = null;
        List<Experiment> experiments = project.getExperiments();
        if(experiments != null) {
            if(experiments.size() < MAX_ALLOWED_EXPERIMENTS) {
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
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                experiments.add(experiment);
                experiment = experimentRepository.save(experiment);
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
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            experiments.add(experiment);
            project.setExperiments(experiments);
            experiment = experimentRepository.save(experiment);
            projectRepository.save(project);
        }
        return experiment;
    }


    public Experiment updateExperiment(User user, Long projectId, Long experimentId, String experimentName) throws PermissionException, ResourceNotFoundException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if(project.getUser().getId() != user.getId()){
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        Experiment experiment = experimentRepository.findById(experimentId).orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));

        experiment.setUpdatedDate(LocalDateTime.now());
        experiment.setName(experimentName);

        experiment = experimentRepository.save(experiment);

        return experiment;
    }

    public Project updateProjectStatus(Long projectId, ProjectStatus status) throws ResourceNotFoundException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        project.setStatus(status);
        project.setUpdatedDate(LocalDateTime.now());

        return projectRepository.save(project);
    }

    public Experiment getExperimentById(User user, Long projectId, Long experimentId) throws ResourceNotFoundException, PermissionException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if(project.getUser().getId() != user.getId()){
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        Experiment experiment = experimentRepository.findById(experimentId).orElseThrow(() -> new ResourceNotFoundException("ERROR_EXPERIMENT_DOES_NOT_EXISTS"));
        return experiment;
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
