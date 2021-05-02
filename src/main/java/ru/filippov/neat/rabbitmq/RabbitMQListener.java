package ru.filippov.neat.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.filippov.neat.dto.services.prediction.ExperimentStatusDto;
import ru.filippov.neat.dto.services.prediction.PredictionResult;
import ru.filippov.neat.dto.services.preprocessing.NormalizationResult;
import ru.filippov.neat.dto.services.preprocessing.VerificationResult;
import ru.filippov.neat.exception.ResourceNotFoundException;
import ru.filippov.neat.service.project.ProjectServiceImpl;

import java.io.IOException;

@Log4j2
@Component
public class RabbitMQListener {

    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private ObjectMapper objectMapper;


    @RabbitListener(queues = "${rabbitmq.input.predictionStatus.queue:prediction-status}")
    public void consumeStatusFromPredictionService(Message message) throws IOException, ResourceNotFoundException {
        ExperimentStatusDto statusDto = objectMapper.readValue(message.getBody(), ExperimentStatusDto.class);
        projectService.updateProjectStatus(statusDto.getProjectId(), statusDto.getStatus());

    }

    @RabbitListener(queues = "${rabbitmq.input.predictionResult.queue:prediction-result}")
    public void consumeResultFromPredictionService(Message message) throws IOException, ResourceNotFoundException {

        PredictionResult predictionResult = objectMapper.readValue(message.getBody(), PredictionResult.class);

        projectService.setPredictionResult(predictionResult);


                //final PredictionServiceResult predictionServiceResult = objectMapper.readValue(message.getBody(), PredictionServiceResult.class);
        //JsonNode jsonNode = objectMapper.readTree(message.getBody());
        /*log.info(serviceResult.toString());*/
    }

    @RabbitListener(queues = "${rabbitmq.input.verificationResult.queue:verification-result}")
    public void consumeResultFromVerificationService(Message message) throws IOException, ResourceNotFoundException {
        VerificationResult verificationResult = objectMapper.readValue(message.getBody(), VerificationResult.class);


        projectService.setVerificationResult(verificationResult);
        //final PredictionServiceResult predictionServiceResult = objectMapper.readValue(message.getBody(), PredictionServiceResult.class);
        //JsonNode jsonNode = objectMapper.readTree(message.getBody());
        /*log.info(serviceResult.toString());*/
    }

    @RabbitListener(queues = "${rabbitmq.input.normalizationResult.queue:normalization-result}")
    public void consumeResultFromNormalizationService(Message message) throws IOException, ResourceNotFoundException {
        NormalizationResult normalizationResult = objectMapper.readValue(message.getBody(), NormalizationResult.class);

        projectService.setNormalizationResult(normalizationResult);

        //final PredictionServiceResult predictionServiceResult = objectMapper.readValue(message.getBody(), PredictionServiceResult.class);
        //JsonNode jsonNode = objectMapper.readTree(message.getBody());
        /*log.info(serviceResult.toString());*/
    }



}
