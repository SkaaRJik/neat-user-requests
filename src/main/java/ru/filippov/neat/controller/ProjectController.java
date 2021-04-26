package ru.filippov.neat.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.filippov.neat.dto.experiment.NewExperimentDto;
import ru.filippov.neat.dto.experiment.UpdateExperimentDto;
import ru.filippov.neat.dto.project.NewProjectDto;
import ru.filippov.neat.entity.Experiment;
import ru.filippov.neat.entity.Project;
import ru.filippov.neat.entity.view.ProjectView;
import ru.filippov.neat.exception.PermissionException;
import ru.filippov.neat.exception.ResourceNotFoundException;
import ru.filippov.neat.exception.project.ProjectException;
import ru.filippov.neat.parser.excel.ExcelParser;
import ru.filippov.neat.service.project.ProjectServiceImpl;
import ru.filippov.neat.service.user.UserDetailsServiceImpl;
import ru.filippov.neat.service.user.UserPrincipal;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(value = "api/projects")
public class ProjectController {

    @Bean
    public ExcelParser getExcelParser(){
        return new ExcelParser();
    }


    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @PostMapping("")
    @JsonView(ProjectView.Info.class)
    public Project saveProject(@AuthenticationPrincipal UserPrincipal user,
                                         @RequestBody NewProjectDto newProjectDto){
        try{
            Project project = projectService.createProject(user.toUser(), newProjectDto.getProjectName());
            return project;
        } catch (ProjectException e) {
            log.error(String.format("ProjectController.saveProject: user: %d-%s ; projectDto: %s ", user.getId(), user.getUsername(), newProjectDto.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            log.error(String.format("ProjectController.saveProject: user: %d-%s ; projectDto: %s ", user.getId(), user.getUsername(), newProjectDto.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping("/{id}/attach")
    @JsonView(ProjectView.Id.class)
    public String attachSourceFileToProject(@AuthenticationPrincipal UserPrincipal user,
                                                       @RequestBody MultipartFile file,
                                                       @PathVariable("id") Long projectId){
        
        try{
            projectService.attachSourceFileToProject(user.toUser(), projectId, file);
            return "INFO_VERIFICATION_STARTED";
        } catch (PermissionException e) {
            log.error(String.format("ProjectController.attachSourceFileToProject: projectId: %d ; user: %d-%s ", projectId, user.getId(), user.getUsername()), e);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (ResourceNotFoundException e) {
            log.error(String.format("ProjectController.attachSourceFileToProject: projectId: %d ; user: %d-%s ", projectId, user.getId(), user.getUsername()), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception ex) {
            log.error(String.format("ProjectController.attachSourceFileToProject: projectId: %d ; user: %d-%s ", projectId, user.getId(), user.getUsername()), ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
        
    }

    @GetMapping("")
    @JsonView(ProjectView.Info.class)
    public Page<Project> getProjects(@AuthenticationPrincipal UserPrincipal user,
                                     @RequestParam(name = "page", defaultValue = "1") Integer page,
                                     @RequestParam(name = "itemsPerPage", defaultValue = "10") Integer itemsPerPage,
                                     @RequestParam(name = "projectBelongsTo", defaultValue = "me") String belongsTo){
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
                                            @PathVariable("id") Long id) throws PermissionException {
        try{
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



    /*@RequestMapping(value = {"/{id}/config", "/{id}/config/{config_id}"},method = RequestMethod.PUT)
    public ResponseEntity<?> saveProjectConfiguration(@AuthenticationPrincipal UserPrincipal user, @PathVariable("id") Long id, @PathVariable(name = "config_id", required = false) Long configId, @RequestBody ProjectConfigDto projectConfig){
        try {
            final boolean b = projectService.saveNeatConfig(id, user.getId(), projectConfig, configId);
            return ResponseEntity.ok().build();
        } catch (ProjectException e) {
            log.error("ProjectController.getProjectData", e);
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (PermissionException e) {
            log.error("ProjectController.getProjectData", e);
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error("ProjectController.getProjectData", e);
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @RequestMapping(value = "/{project_id}/experiment",method = RequestMethod.POST)
    public Experiment createExperiment(@AuthenticationPrincipal UserPrincipal user,
                                              @PathVariable("project_id") Long projectId,
                                              @RequestBody NewExperimentDto newExperimentDto){
        try {
            Experiment experiment = projectService.createExperiment(user.toUser(), projectId, newExperimentDto.getName());
            return experiment;
        } catch (ResourceNotFoundException e) {
            log.error(String.format("ProjectController.createExperiment: projectId: %d ; user: %d-%s ; newExperimentDto = %s", projectId, user.getId(), user.getUsername(), newExperimentDto.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (PermissionException e) {
            log.error(String.format("ProjectController.createExperiment: projectId: %d ; user: %d-%s ; newExperimentDto = %s", projectId, user.getId(), user.getUsername(), newExperimentDto.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            log.error(String.format("ProjectController.createExperiment: projectId: %d ; user: %d-%s ; newExperimentDto = %s", projectId, user.getId(), user.getUsername(), newExperimentDto.toString()), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        
    }

    @RequestMapping(value = "/{id}/experiment/{experiment_id}",method = RequestMethod.PUT)
    public Experiment updateExperiment(@AuthenticationPrincipal UserPrincipal user,
                                              @PathVariable("project_id") Long projectId,
                                              @PathVariable(name = "experiment_id", required = false) Long experimentId,
                                              @RequestBody UpdateExperimentDto updateExperimentDto){
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

    @RequestMapping(value = "/{id}/experiment/{experiment_id}",method = RequestMethod.POST)
    public String normalizeDataForExperiment(@AuthenticationPrincipal UserPrincipal user,
                                              @PathVariable("project_id") Long projectId,
                                              @PathVariable(name = "experiment_id", required = false) Long experimentId,
                                              @RequestBody UpdateExperimentDto updateExperimentDto){
        try {
            projectService.normalizeData(user.toUser(), projectId, experimentId, updateExperimentDto.getNormalizationMethod(), updateExperimentDto.getEnableLogTransform());
            return "INFO_NORMALIZATION_STARTED";
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



}
