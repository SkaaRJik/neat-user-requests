package ru.filippov.neatvue.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("")
public class MainPageController {
    @GetMapping
    public String index(){

        return "index.html";
    }
}
