package ru.filippov.neat.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.filippov.neat.dto.experiment.UpdateExperimentDto;
import ru.filippov.neat.dto.services.prediction.ExperimentData;
import ru.filippov.neat.entity.Experiment;
import ru.filippov.neat.entity.Project;
import ru.filippov.neat.entity.view.ExperimentView;
import ru.filippov.neat.entity.view.ProjectView;
import ru.filippov.neat.exception.PermissionException;
import ru.filippov.neat.exception.ResourceNotFoundException;
import ru.filippov.neat.exception.project.ProjectException;
import ru.filippov.neat.parser.excel.ExcelParser;
import ru.filippov.neat.service.project.ProjectServiceImpl;
import ru.filippov.neat.service.user.UserDetailsServiceImpl;
import ru.filippov.neat.service.user.UserPrincipal;

@RestController
@Slf4j
@RequestMapping(value = "api/projects")
public class ProjectController {

    @Bean
    public ExcelParser getExcelParser() {
        return new ExcelParser();
    }


    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @PostMapping("")
    @JsonView(ProjectView.FullInfo.class)
    public Project saveProject(@AuthenticationPrincipal UserPrincipal user,
                               @RequestParam("file") MultipartFile sourceFile,
                               @RequestParam("projectName") String projectName
    ) {
        try {
            Project project = projectService.createProject(user.toUser(), projectName, sourceFile);
            return project;
        } catch (ProjectException e) {
            log.error(String.format("ProjectController.saveProject: user: %d-%s ; projectDto: %s ", user.getId(), user.getUsername(), projectName), e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            log.error(String.format("ProjectController.saveProject: user: %d-%s ; projectDto: %s ", user.getId(), user.getUsername(), projectName), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping("/{id}/attach")
    @JsonView(ProjectView.FullInfo.class)
    public Project attachSourceFileToProject(@AuthenticationPrincipal UserPrincipal user,
                                             @RequestParam("file") MultipartFile file,
                                             @PathVariable("id") Long projectId) {

        try {
            return projectService.attachSourceFileToProject(user.toUser(), projectId, file);
        } catch (PermissionException e) {
            log.error(String.format("ProjectController.attachSourceFileToProject: projectId: %d ; user: %d-%s ", projectId, user.getId(), user.getUsername()), e);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (ResourceNotFoundException e) {
            log.error(String.format("ProjectController.attachSourceFileToProject: projectId: %d ; user: %d-%s ", projectId, user.getId(), user.getUsername()), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ProjectException e) {
            log.error(String.format("ProjectController.attachSourceFileToProject: projectId: %d ; user: %d-%s ", projectId, user.getId(), user.getUsername()), e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception ex) {
            log.error(String.format("ProjectController.attachSourceFileToProject: projectId: %d ; user: %d-%s ", projectId, user.getId(), user.getUsername()), ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }

    }

    @GetMapping("")
    public Page<Project> getProjects(@AuthenticationPrincipal UserPrincipal user,
                                     @RequestParam(name = "page", defaultValue = "1") Integer page,
                                     @RequestParam(name = "itemsPerPage", defaultValue = "10") Integer itemsPerPage,
                                     @RequestParam(name = "projectBelongsTo", defaultValue = "me") String belongsTo) {
        Page<Project> projectsByUser = projectService.getProjectsByUser(user.toUser(), page, itemsPerPage);
        return projectsByUser;
    }

    /*@GetMapping("")
    public ResponseEntity<?> findProject(@AuthenticationPrincipal UserPrincipal user, @RequestParam MultiValueMap<String, String> allParams) throws PermissionException {


        String projectName = allParams.getFirst("projectName");

        String userName = allParams.getFirst("user");

        User userForQuery = user.toUser();

        if (userName != null){
            userForQuery = userDetailsService.loadUserByUsername(userName).toUser();
        }

        Project projectsByNameAndUser = projectService.getProjectsByNameAndUser(projectName, userForQuery);

        return ResponseEntity.ok(projectsByNameAndUser);
    }
*/

    @GetMapping("/{id}")
    @JsonView(ProjectView.FullInfo.class)
    public Project getProjectInfo(@AuthenticationPrincipal UserPrincipal user,
                                  @PathVariable("id") Long id) {
        try {
            Project projectsById = projectService.getProjectById(id, user.toUser());
            return projectsById;
        } catch (ResourceNotFoundException ex) {
            log.error(String.format("ProjectController.getProjectInfo: projectId: %d ; user: %d-%s ", id, user.getId(), user.getUsername()), ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (PermissionException ex) {
            log.error(String.format("ProjectController.getProjectInfo: projectId: %d ; user: %d-%s ", id, user.getId(), user.getUsername()), ex);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(String.format("ProjectController.getProjectInfo: projectId: %d ; user: %d-%s ", id, user.getId(), user.getUsername()), ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }

    }


    @JsonView(ExperimentView.FullInfo.class)
    @RequestMapping(value = "/{project_id}/experiment/{experiment_id}/predict", method = RequestMethod.POST)
    public Experiment startPrediction(@AuthenticationPrincipal UserPrincipal user,
                                      @PathVariable("project_id") Long projectId,
                                      @PathVariable(name = "experiment_id") Long experimentId,
                                      @RequestBody ExperimentData experimentData) {
        try {
            return projectService.startPrediction(user.toUser(), projectId, experimentId, experimentData);
        } catch (ResourceNotFoundException e) {
            log.error(String.format("ProjectController.startPrediction: projectId: %d ; user: %d-%s ; experimentId: %d; experimentData = %s", projectId, user.getId(), user.getUsername(), experimentId, experimentData.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (PermissionException e) {
            log.error(String.format("ProjectController.startPrediction: projectId: %d ; user: %d-%s ; experimentId: %d; experimentData = %s", projectId, user.getId(), user.getUsername(), experimentId, experimentData.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            log.error(String.format("ProjectController.startPrediction: projectId: %d ; user: %d-%s ; experimentId: %d; experimentData = %s", projectId, user.getId(), user.getUsername(), experimentId, experimentData.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }


    @RequestMapping(value = "/{project_id}/experiment/{experiment_id}", method = RequestMethod.GET)
    public Experiment getExperiment(@AuthenticationPrincipal UserPrincipal user,
                                    @PathVariable("project_id") Long projectId,
                                    @PathVariable(name = "experiment_id") Long experimentId) {
        try {
            Experiment experimentById = projectService.getExperimentById(user.toUser(), projectId, experimentId);
            return experimentById;
        } catch (ResourceNotFoundException e) {
            log.error(String.format("ProjectController.getExperiment: projectId: %d ; user: %d-%s ; experimentId: %d; ", projectId, user.getId(), user.getUsername(), experimentId), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (PermissionException e) {
            log.error(String.format("ProjectController.getExperiment: projectId: %d ; user: %d-%s ; experimentId: %d; ", projectId, user.getId(), user.getUsername(), experimentId), e);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            log.error(String.format("ProjectController.getExperiment: projectId: %d ; user: %d-%s ; experimentId: %d; ", projectId, user.getId(), user.getUsername(), experimentId), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @JsonView(ExperimentView.FullInfo.class)
    @RequestMapping(value = "/{project_id}/experiment", method = RequestMethod.POST)
    public Experiment createExperiment(@AuthenticationPrincipal UserPrincipal user,
                                       @PathVariable("project_id") Long projectId) {
        try {
            Experiment experiment = projectService.createExperiment(user.toUser(), projectId);
            return experiment;
        } catch (ResourceNotFoundException e) {
            log.error(String.format("ProjectController.createExperiment: projectId: %d ; user: %d-%s ;", projectId, user.getId(), user.getUsername()), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (PermissionException e) {
            log.error(String.format("ProjectController.createExperiment: projectId: %d ; user: %d-%s ;", projectId, user.getId(), user.getUsername()), e);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            log.error(String.format("ProjectController.createExperiment: projectId: %d ; user: %d-%s ;", projectId, user.getId(), user.getUsername()), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @JsonView(ExperimentView.Id.class)
    @RequestMapping(value = {"/{project_id}/experiment/{experiment_id}/copy",
            "/{project_id}/experiment/{experiment_id}/copy/{rewrite_experiment_id}"},
            method = RequestMethod.POST)
    public Experiment copyExperiment(@AuthenticationPrincipal UserPrincipal user,
                                     @PathVariable("project_id") Long projectId,
                                     @PathVariable(name = "experiment_id") Long experimentId,
                                     @PathVariable(name = "rewrite_experiment_id", required = false) Long rewriteExperimentId
    ) {
        try {
            final Experiment experiment = projectService.copyExperiment(user.toUser(), projectId, experimentId, rewriteExperimentId);
            return experiment;
        } catch (ResourceNotFoundException e) {
            log.error(String.format("ProjectController.copyExperiment: projectId: %d ; user: %d-%s ; experimentId: %d; rewriteExperimentId = %d", projectId, user.getId(), user.getUsername(), experimentId, rewriteExperimentId), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (PermissionException e) {
            log.error(String.format("ProjectController.copyExperiment: projectId: %d ; user: %d-%s ; experimentId: %d; rewriteExperimentId = %d", projectId, user.getId(), user.getUsername(), experimentId, rewriteExperimentId), e);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            log.error(String.format("ProjectController.copyExperiment: projectId: %d ; user: %d-%s ; experimentId: %d; rewriteExperimentId = %d", projectId, user.getId(), user.getUsername(), experimentId, rewriteExperimentId), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

    }

    @JsonView(ExperimentView.FullInfo.class)
    @RequestMapping(value = "/{project_id}/experiment/{experiment_id}", method = RequestMethod.PUT)
    public Experiment updateExperiment(@AuthenticationPrincipal UserPrincipal user,
                                       @PathVariable("project_id") Long projectId,
                                       @PathVariable(name = "experiment_id") Long experimentId,
                                       @RequestBody UpdateExperimentDto updateExperimentDto) {
        try {
            final Experiment experiment = projectService.updateExperiment(user.toUser(), projectId, experimentId, updateExperimentDto.getName());
            return experiment;
        } catch (ResourceNotFoundException e) {
            log.error(String.format("ProjectController.updateExperiment: projectId: %d ; user: %d-%s ; experimentId: %d; updateExperimentDto = %s", projectId, user.getId(), user.getUsername(), experimentId, updateExperimentDto.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (PermissionException e) {
            log.error(String.format("ProjectController.updateExperiment: projectId: %d ; user: %d-%s ; experimentId: %d; updateExperimentDto = %s", projectId, user.getId(), user.getUsername(), experimentId, updateExperimentDto.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            log.error(String.format("ProjectController.updateExperiment: projectId: %d ; user: %d-%s ; experimentId: %d; updateExperimentDto = %s", projectId, user.getId(), user.getUsername(), experimentId, updateExperimentDto.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

    }

    @JsonView(ExperimentView.FullInfo.class)
    @RequestMapping(value = "/{project_id}/experiment/{experiment_id}/normalize", method = RequestMethod.POST)
    public Experiment normalizeDataForExperiment(@AuthenticationPrincipal UserPrincipal user,
                                                 @PathVariable("project_id") Long projectId,
                                                 @PathVariable(name = "experiment_id", required = false) Long experimentId,
                                                 @RequestBody UpdateExperimentDto updateExperimentDto) {
        try {
            return projectService.normalizeData(user.toUser(), projectId, experimentId, updateExperimentDto.getNormalizationMethod(), updateExperimentDto.getEnableLogTransform());
        } catch (ResourceNotFoundException e) {
            log.error(String.format("ProjectController.updateExperiment: projectId: %d ; user: %d-%s ; experimentId: %d; updateExperimentDto = %s", projectId, user.getId(), user.getUsername(), experimentId, updateExperimentDto.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (PermissionException e) {
            log.error(String.format("ProjectController.updateExperiment: projectId: %d ; user: %d-%s ; experimentId: %d; updateExperimentDto = %s", projectId, user.getId(), user.getUsername(), experimentId, updateExperimentDto.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            log.error(String.format("ProjectController.updateExperiment: projectId: %d ; user: %d-%s ; experimentId: %d; updateExperimentDto = %s", projectId, user.getId(), user.getUsername(), experimentId, updateExperimentDto.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping(value = "/{project_id}/experiment/{experiment_id}/download_report")
    public ResponseEntity downloadReport(@AuthenticationPrincipal UserPrincipal user,
                                                            @PathVariable("project_id") Long projectId,
                                                            @PathVariable(name = "experiment_id") Long experimentId) {
        try {
            byte[] experimentReport = projectService.getExperimentReport(user.toUser(), projectId, experimentId);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=customers.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(new ByteArrayResource(experimentReport));
        } catch (ResourceNotFoundException e) {
            log.error(String.format("ProjectController.downloadReport: projectId: %d ; user: %d-%s ; experimentId: %d", projectId, user.getId(), user.getUsername(), experimentId), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (PermissionException e) {
            log.error(String.format("ProjectController.downloadReport: projectId: %d ; user: %d-%s ; experimentId: %d", projectId, user.getId(), user.getUsername(), experimentId), e);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            log.error(String.format("ProjectController.downloadReport: projectId: %d ; user: %d-%s ; experimentId: %d", projectId, user.getId(), user.getUsername(), experimentId), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }


}
