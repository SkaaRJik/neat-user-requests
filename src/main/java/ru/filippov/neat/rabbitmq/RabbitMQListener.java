package ru.filippov.neat.rabbitmq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.filippov.neat.config.RabbitConfig;
import ru.filippov.neat.dto.services.prediction.ExperimentStatusDto;
import ru.filippov.neat.dto.services.prediction.PredictionServiceResult;
import ru.filippov.neat.entity.ProjectStatus;
import ru.filippov.neat.exception.ProjectNotFoundException;
import ru.filippov.neat.service.project.ProjectServiceImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class RabbitMQListener {

    @Autowired
    private ProjectServiceImpl projectService;


    @RabbitListener(queues = "${rabbitmq.input.queue.status:status}")
    public void consumeStatusFromPredictionService(ExperimentStatusDto statusDto) throws IOException {

        try {
            projectService.updateProjectStatus(statusDto.getProjectId(), ProjectStatus.valueOf(statusDto.getStatus()));
        } catch (ProjectNotFoundException e) {
            log.error(String.format("[statusDto] = %s", statusDto.toString()), e);
        }


    }

    @RabbitListener(queues = "${rabbitmq.input.queue.result:result}")
    public void consumeResultFromPredictionService(PredictionServiceResult serviceResult) throws IOException {

        //final PredictionServiceResult predictionServiceResult = objectMapper.readValue(message.getBody(), PredictionServiceResult.class);
        //JsonNode jsonNode = objectMapper.readTree(message.getBody());
        log.info(serviceResult.toString());



    }



}
