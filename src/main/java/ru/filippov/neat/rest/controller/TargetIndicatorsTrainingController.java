package ru.filippov.neat.rest.controller;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.filippov.utils.NEATConfig;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/trainer")
public class TargetIndicatorsTrainingController {

    @GetMapping("/default-config")
    public List<Map> getDefaultConfig(){
        return NEATConfig.getDefaultConfig();
    }

    @PostMapping("/save")
    public void saveConfig(@RequestBody NEATConfig config){

        config.getSettings().forEach((o, o2) -> {
            System.out.println(o + " = " + o2);
        });


        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    }

    @GetMapping("/activation-functions")
    public List<String> getActivationFunctions(){
        return List.of("org.neat4j.neat.nn.core.functions.LinearFunction",
                "org.neat4j.neat.nn.core.functions.SigmoidFunction",
                "org.neat4j.neat.nn.core.functions.TanhFunction");
    }


}
