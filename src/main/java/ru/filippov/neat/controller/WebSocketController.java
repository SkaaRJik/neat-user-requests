package ru.filippov.neat.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.filippov.neat.entity.Experiment;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
    @SendToUser("/queue/reply")
    public Experiment sendExperiment(Experiment experiment){

        String username = experiment.getProject().getUser().getUsername();
        return experiment;
        /*messagingTemplate.convertAndSendToUser(username, "/queue/reply", experiment);*/
    }


}
