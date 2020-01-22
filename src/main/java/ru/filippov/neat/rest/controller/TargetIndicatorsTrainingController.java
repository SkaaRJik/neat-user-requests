package ru.filippov.neat.rest.controller;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.filippov.utils.AIConfig;
import ru.filippov.utils.NEATConfig;

@RestController
@RequestMapping("api/trainer")
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


        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    }


}
