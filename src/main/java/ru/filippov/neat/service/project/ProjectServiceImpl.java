package ru.filippov.neat.service.project;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.filippov.neat.dto.ProjectConfigDto;
import ru.filippov.neat.entity.NeatConfig;
import ru.filippov.neat.entity.Project;
import ru.filippov.neat.entity.ProjectStatus;
import ru.filippov.neat.entity.User;
import ru.filippov.neat.entity.view.ProjectView;
import ru.filippov.neat.exception.NeatConfigurationNotFoundException;
import ru.filippov.neat.exception.PermissionException;
import ru.filippov.neat.exception.ProjectNotFoundException;
import ru.filippov.neat.rabbitmq.RabbitMQWriter;
import ru.filippov.neat.repository.NeatConfigRepository;
import ru.filippov.neat.repository.ProjectRepository;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ProjectServiceImpl {
    private final ProjectRepository projectRepository;

    @Value("${path.projects.data:./public/projects}")
    private String projectsLocation;

    @Value("${app.project.restrictions.max_allowed_neat_config:5}")
    private int max_allowed_neat_config;

    @Autowired
    private RabbitMQWriter rabbitMQWriter;

    private final NeatConfigRepository neatConfigRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, NeatConfigRepository neatConfigRepository) {
        this.projectRepository = projectRepository;
        this.neatConfigRepository = neatConfigRepository;
    }

    public Project saveProject(User user, Map<String, Object> projectParams) throws IOException {

        Project project = null;

        if (projectParams.containsKey("id")) {
            project = projectRepository
                    .findById((Long) projectParams.get("id"))
                    .orElseThrow(() -> new NoSuchElementException(String.format("Project with id %s does not exist", projectParams.get("id"))));

        } else {
            project = Project.builder()
                    .name((String) projectParams.get("name"))
                    .user(user)
                    .creationDate(LocalDateTime.now())
                    .status(ProjectStatus.NEW)
                    .build();
        }


        project.setUpdatedDate(LocalDateTime.now());
        project.setData(projectParams);


        project = projectRepository.save(project);


        return project;
    }

    public Page<Project> getProjectsByUser(User user, int curPage, int itemsPerPage) {
        Pageable pageable = PageRequest.of(curPage, itemsPerPage);

        Page<Project> projects = projectRepository.findAllByUserId(user.getId(), pageable);

        return projects;
    }


    public Project getProjectById(Long id, User user) throws PermissionException, NoSuchElementException {

        Project project = projectRepository.findById(id).orElseThrow(() -> new NoSuchElementException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if(project.getUser().getId() != user.getId()){
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        return project;
    }

    public Project getProjectsByNameAndUser(String name, User user) throws PermissionException, NoSuchElementException {

        Project project = projectRepository.findOneByNameAndUserId(name, user.getId()).orElse(null);

        /*if(project.getUser().getId() != user.getId()){
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }*/



        return project;
    }

    public Map getProjectData(Long id, User user) throws IOException, ClassNotFoundException, PermissionException {

        Project project = projectRepository.findById(id).orElse(null);

        User projectUser = project.getUser();

        if(projectUser.getId() != user.getId()){
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        Map data = project.getData();;

        return data;
    }

    public boolean saveNeatConfig(Long projectId, Long userId, ProjectConfigDto params, Long configId) throws ProjectNotFoundException, PermissionException, NeatConfigurationNotFoundException, JsonProcessingException {

        final Project project = projectRepository
                .findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if(project.getUser().getId() != userId) {
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        project.setStatus(ProjectStatus.IN_QUEUE);
        projectRepository.save(project);

        NeatConfig neatConfig = null;

        if(configId != null) {
            neatConfig = neatConfigRepository
                    .findById(configId)
                    .orElseThrow(NeatConfigurationNotFoundException::new);

            neatConfig = updateNeatConfig(params, neatConfig);
        } else {
            final List<NeatConfig> neatConfigs = project.getNeatConfigs();

            if(neatConfigs.size() >= max_allowed_neat_config) {
                neatConfigs.sort(Comparator.comparing(NeatConfig::getCreationDate).reversed());
                neatConfig = neatConfigs.get(0);
                neatConfig = updateNeatConfig(params, neatConfig);
            } else {
                neatConfig = new NeatConfig(
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
                neatConfig = neatConfigRepository.save(neatConfig);
            }
        }

        //rabbitMQWriter.writeIntoExperimentServerQueue(neatConfig.toExperimentData());



        return true;
    }

    private NeatConfig updateNeatConfig(ProjectConfigDto params, NeatConfig neatConfig) {
        neatConfig.setCreationDate(LocalDateTime.now());
        neatConfig.setNormalizedData(params.getNormalizedData());
        neatConfig.setNeatSettings(params.getSettings());
        neatConfig.setPredictionWindowSize(params.getWindowSize());
        neatConfig.setPredictionPeriod(params.getPredictionPeriod());

        return neatConfigRepository.save(neatConfig);
    }

}
