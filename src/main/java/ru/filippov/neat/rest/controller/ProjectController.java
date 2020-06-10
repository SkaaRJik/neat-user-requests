package ru.filippov.neat.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.filippov.neat.config.jwt.JwtProvider;
import ru.filippov.neat.parser.excel.ExcelParser;
import ru.filippov.neat.repository.UserRepository;
import ru.filippov.neat.service.project.ProjectServiceImpl;
import ru.filippov.neat.service.user.UserDetailsServiceImpl;
import ru.filippov.neat.service.user.UserPrinciple;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "api/projects")
public class ProjectController {



    @Bean
    public ExcelParser getExcelParser(){
        return new ExcelParser();
    }

    @Autowired
    ExcelParser excelParser;

    @Autowired
    JwtProvider tokenProvider;

    @Autowired
    ProjectServiceImpl projectService;

    @PostMapping("/parse")
    public ResponseEntity<?> parseExcel(@RequestBody MultipartFile file){

        try {
            return ResponseEntity.ok(excelParser.parseFile(file));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("CANT_PROCESS_FILE", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> parseExcel(@AuthenticationPrincipal UserPrinciple user, @RequestBody Map<String,Object> params){


        if (params.containsKey("id")){

        } else {

        }

        /*try {
            return ResponseEntity.ok(excelParser.parseFile(file));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("CANT_PROCESS_FILE", HttpStatus.INTERNAL_SERVER_ERROR);
        }*/

        return null;

    }
}
