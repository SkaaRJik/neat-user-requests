package ru.filippov.neat.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.filippov.neat.dto.ProjectConfigDto;
import ru.filippov.neat.entity.Project;
import ru.filippov.neat.entity.User;
import ru.filippov.neat.entity.view.ProjectView;
import ru.filippov.neat.exception.PermissionException;
import ru.filippov.neat.exception.ProjectNotFoundException;
import ru.filippov.neat.parser.excel.ExcelParser;
import ru.filippov.neat.service.project.ProjectServiceImpl;
import ru.filippov.neat.service.user.UserDetailsServiceImpl;
import ru.filippov.neat.service.user.UserPrincipal;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequestMapping(value = "api/projects")
public class ProjectController {



    @Bean
    public ExcelParser getExcelParser(){
        return new ExcelParser();
    }

    @Autowired
    private ExcelParser excelParser;

    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/parse")
    public ResponseEntity<?> parseExcel(@RequestBody MultipartFile file){

        try {
            return ResponseEntity.ok(excelParser.parseFile(file));
        } catch (Exception e) {
            log.error("ProjectController.parseExcel", e);
            return new ResponseEntity<String>("ERROR_CANT_PROCESS_FILE", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveProject(@AuthenticationPrincipal UserPrincipal user, @RequestBody Map<String,Object> params){
        Project project = null;
        try{
            project = projectService.saveProject(user.toUser(), params);
        } catch (Exception ex) {
            log.error("ProjectController.saveProject", ex);
            return new ResponseEntity<String>("ERROR_CANT_SAVE", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Long>(project.getId(), HttpStatus.OK);
    }

    @GetMapping("/my")
    public Page<Project> getProjects(@AuthenticationPrincipal UserPrincipal user, @RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(name = "itemsPerPage", defaultValue = "10") Integer itemsPerPage){
        Page<Project> projectsByUser = projectService.getProjectsByUser(user.toUser(), page, itemsPerPage);
        return projectsByUser;
    }

    @GetMapping("/find")
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


    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectInfo(@AuthenticationPrincipal UserPrincipal user, @PathVariable("id") Long id) throws PermissionException {
        try{
            Project projectsByUser = projectService.getProjectById(id, user.toUser());
            return new ResponseEntity<Project>(projectsByUser, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (PermissionException ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception ex) {
            log.error("ProjectController.getProjectInfo", ex);
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/data")
    public ResponseEntity<?> getProjectData(@AuthenticationPrincipal UserPrincipal user, @PathVariable("id") Long id) throws PermissionException {
        try{
            Map projectData = projectService.getProjectData(id, user.toUser());
            return new ResponseEntity<Map>(projectData, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (PermissionException ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception ex) {
            log.error("ProjectController.getProjectData", ex);
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = {"/{id}/config", "/{id}/config/{config_id}"},method = RequestMethod.PUT)
    public ResponseEntity<?> saveProjectConfiguration(@AuthenticationPrincipal UserPrincipal user, @PathVariable("id") Long id, @PathVariable(name = "config_id", required = false) Long configId, @RequestBody ProjectConfigDto projectConfig){
        try {
            final boolean b = projectService.saveNeatConfig(id, user.getId(), projectConfig, configId);
            return ResponseEntity.ok().build();
        } catch (ProjectNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (PermissionException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error("ProjectController.getProjectData", e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    };




}
