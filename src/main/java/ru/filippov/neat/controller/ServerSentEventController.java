package ru.filippov.neat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.filippov.neat.entity.Experiment;
import ru.filippov.neat.service.user.UserPrincipal;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Data
@RestController
@RequestMapping("/sse")
public class ServerSentEventController {

    Map<Long, SseEmitter> connectedUsers = new ConcurrentHashMap<>(100);

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/connect")
    public SseEmitter connectUser(@AuthenticationPrincipal UserPrincipal user) {
        SseEmitter oldEmitter = connectedUsers.get(user.getId());

        if(oldEmitter != null){
            this.disconnectUser(user.getId());
        }

        SseEmitter emitter = new SseEmitter(-1L);

        emitter.onCompletion(() -> {
            this.connectedUsers.remove(user.getId());
        });

        emitter.onTimeout(()-> {
            emitter.complete();
        });

        connectedUsers.put(user.getId(), emitter);
        /*ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                for (int i = 0; true; i++) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data("SSE MVC - " + LocalTime.now().toString())
                            .id(String.valueOf(i))
                            .name("sse event - mvc");
                    emitter.send(event);
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });*/
        /*try {
            emitter.send(SseEmitter.event().name("connected").build());
        } catch (IOException e) {
            log.error("[ServerSentEventController].connect user: " + user.getId(), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }*/
        return emitter;
    }

    public void disconnectUser(Long userId) {

        SseEmitter sseEmitter = connectedUsers.get(userId);
        if(sseEmitter != null){
            try {
                sseEmitter.complete();
            } catch (Exception ex){
                log.error(ex);
                this.connectedUsers.remove(userId);
            }

        }


    }

    public void updateExperiment(Long userId, Experiment experiment)  {
        SseEmitter sseEmitter = connectedUsers.get(userId);
        if(sseEmitter != null){
            try {
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .data(experiment)
                    .name("experiment");

                sseEmitter.send(event);

            } catch (IOException e) {
                this.disconnectUser(userId);
                log.error("[ServerSentEventController].updateExperiment experiment: " + experiment.toString(), e);
            }
        }
    }

}
