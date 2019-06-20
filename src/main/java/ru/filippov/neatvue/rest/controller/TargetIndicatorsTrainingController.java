package ru.filippov.neatvue.rest.controller;


import org.springframework.web.bind.annotation.*;
import ru.filippov.utils.AIConfig;
import ru.filippov.utils.NEATConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("trainer")
public class TargetIndicatorsTrainingController {

    @GetMapping("/default-config")
    public AIConfig getDefaultConfig(){
        return NEATConfig.getDefaultConfig();
    }

    @PostMapping("/save")
    public void saveConfig(@RequestBody NEATConfig config){

        config.getSettings().forEach((o, o2) -> {
            System.out.println(o + " = " + o2);
        });
    }


}
