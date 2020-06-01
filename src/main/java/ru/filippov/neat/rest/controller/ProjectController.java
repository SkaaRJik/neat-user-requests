package ru.filippov.neat.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.filippov.neat.parser.excel.ExcelParser;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping(value = "api/projects")
public class ProjectController {

    @Bean
    public ExcelParser getExcelParser(){
        return new ExcelParser();
    }


    @PostMapping("/parse")
    public ResponseEntity<?> parseExcel(@RequestBody MultipartFile file){

        try {
            return ResponseEntity.ok(getExcelParser().parseFile(file));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("CANT_PROCESS_FILE", HttpStatus.NOT_ACCEPTABLE);
        }


    }
}
