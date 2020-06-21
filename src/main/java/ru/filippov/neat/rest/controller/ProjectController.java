package ru.filippov.neat.rest.controller;

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
import ru.filippov.neat.domain.Project;
import ru.filippov.neat.domain.User;
import ru.filippov.neat.exceptions.PermissionException;
import ru.filippov.neat.parser.excel.ExcelParser;
import ru.filippov.neat.service.project.ProjectServiceImpl;
import ru.filippov.neat.service.user.UserDetailsServiceImpl;
import ru.filippov.neat.service.user.UserPrincipal;

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
            e.printStackTrace();
            return new ResponseEntity<String>("ERROR_CANT_PROCESS_FILE", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveProject(@AuthenticationPrincipal UserPrincipal user, @RequestBody Map<String,Object> params){
        Project project = null;
        try{
            project = projectService.saveProject(user.toUser(), params);
        } catch (Exception ex) {
            ex.printStackTrace();
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
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
