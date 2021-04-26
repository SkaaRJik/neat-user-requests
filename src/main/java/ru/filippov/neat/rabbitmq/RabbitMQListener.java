package ru.filippov.neat.rabbitmq;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.filippov.neat.dto.services.prediction.ExperimentStatusDto;
import ru.filippov.neat.dto.services.prediction.PredictionServiceResult;
import ru.filippov.neat.dto.services.preprocessing.NormalizationResult;
import ru.filippov.neat.dto.services.preprocessing.VerificationResult;
import ru.filippov.neat.entity.ProjectStatus;
import ru.filippov.neat.exception.ResourceNotFoundException;
import ru.filippov.neat.service.project.ProjectServiceImpl;

import java.io.IOException;

@Log4j2
@Component
public class RabbitMQListener {

    @Autowired
    private ProjectServiceImpl projectService;


    @RabbitListener(queues = "${rabbitmq.input.predictionStatus.queue:prediction-status}")
    public void consumeStatusFromPredictionService(ExperimentStatusDto statusDto) throws IOException {

        try {
            projectService.updateProjectStatus(statusDto.getProjectId(), ProjectStatus.valueOf(statusDto.getStatus()));
        } catch (ResourceNotFoundException e) {
            log.error(String.format("[statusDto] = %s", statusDto.toString()), e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.input.predictionResult.queue:prediction-result}")
    public void consumeResultFromPredictionService(PredictionServiceResult serviceResult) throws IOException {

        //final PredictionServiceResult predictionServiceResult = objectMapper.readValue(message.getBody(), PredictionServiceResult.class);
        //JsonNode jsonNode = objectMapper.readTree(message.getBody());
        /*log.info(serviceResult.toString());*/
    }

    @RabbitListener(queues = "${rabbitmq.input.verificationResult.queue:verification-result}")
    public void consumeResultFromVerificationService(VerificationResult verificationResult) throws IOException {

        //final PredictionServiceResult predictionServiceResult = objectMapper.readValue(message.getBody(), PredictionServiceResult.class);
        //JsonNode jsonNode = objectMapper.readTree(message.getBody());
        /*log.info(serviceResult.toString());*/
    }

    @RabbitListener(queues = "${rabbitmq.input.normalizationResult.queue:normalization-result}")
    public void consumeResultFromNormalizationService(NormalizationResult normalizationResult) throws IOException {

        //final PredictionServiceResult predictionServiceResult = objectMapper.readValue(message.getBody(), PredictionServiceResult.class);
        //JsonNode jsonNode = objectMapper.readTree(message.getBody());
        /*log.info(serviceResult.toString());*/
    }



}
