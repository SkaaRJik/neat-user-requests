package ru.filippov.neat.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.filippov.neat.dto.services.prediction.ExperimentNewStatusDto;
import ru.filippov.neat.dto.services.prediction.PredictionResult;
import ru.filippov.neat.dto.services.preprocessing.NormalizationResult;
import ru.filippov.neat.dto.services.preprocessing.ReportResult;
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
        try {
            ExperimentNewStatusDto statusDto = objectMapper.readValue(message.getBody(), ExperimentNewStatusDto.class);
            projectService.updateExperimentStatus(statusDto);
        } catch (Exception ex) {
            log.error(new StringBuilder("RabbitMQListener.consumeStatusFromPredictionService Message.body: ").append(message.getBody()).toString(), ex);
        }
    }

    @RabbitListener(queues = "${rabbitmq.input.predictionResult.queue:prediction-result}")
    public void consumeResultFromPredictionService(Message message) throws IOException, ResourceNotFoundException {
        try {
            PredictionResult predictionResult = objectMapper.readValue(message.getBody(), PredictionResult.class);
            projectService.setPredictionResult(predictionResult);
        } catch (Exception ex) {
            log.error(new StringBuilder("RabbitMQListener.consumeResultFromPredictionService Message.body: ").append(message.getBody()).toString(), ex);
        }

    }

    @RabbitListener(queues = "${rabbitmq.input.verificationResult.queue:verification-result}")
    public void consumeResultFromVerificationService(Message message) throws IOException, ResourceNotFoundException {
        try {
            VerificationResult verificationResult = objectMapper.readValue(message.getBody(), VerificationResult.class);
            projectService.setVerificationResult(verificationResult);
        } catch (IOException ex) {
            log.error(new StringBuilder("RabbitMQListener.consumeResultFromVerificationService Message.body: ").append(message.getBody()).toString(), ex);
        } catch (ResourceNotFoundException ex) {
            log.error(new StringBuilder("RabbitMQListener.consumeResultFromVerificationService Message.body: ").append(message.getBody()).toString(), ex);
        } catch (Exception ex) {
            log.error(new StringBuilder("RabbitMQListener.consumeResultFromVerificationService Message.body: ").append(message.getBody()).toString(), ex);
        }
    }

    @RabbitListener(queues = "${rabbitmq.input.normalizationResult.queue:normalization-result}")
    public void consumeResultFromNormalizationService(Message message) throws IOException, ResourceNotFoundException {
        try {
            NormalizationResult normalizationResult = objectMapper.readValue(message.getBody(), NormalizationResult.class);
            projectService.setNormalizationResult(normalizationResult);
        } catch (IOException ex) {
            log.error(new StringBuilder("RabbitMQListener.consumeResultFromNormalizationService Message.body: ").append(message.getBody()).toString(), ex);
        } catch (ResourceNotFoundException ex) {
            log.error(new StringBuilder("RabbitMQListener.consumeResultFromNormalizationService Message.body: ").append(message.getBody()).toString(), ex);
        } catch (Exception ex) {
            log.error(new StringBuilder("RabbitMQListener.consumeResultFromNormalizationService Message.body: ").append(message.getBody()).toString(), ex);
        }
    }

    @RabbitListener(queues = "${rabbitmq.input.reportResult.queue:report-result}")
    public void consumeResultFromReportService(Message message) throws IOException, ResourceNotFoundException {
        try {
            ReportResult reportResult = objectMapper.readValue(message.getBody(), ReportResult.class);
            projectService.setReportResult(reportResult);
        } catch (IOException ex) {
            log.error(new StringBuilder("RabbitMQListener.consumeResultFromReportService Message.body: ").append(message.getBody()).toString(), ex);
        } catch (ResourceNotFoundException ex) {
            log.error(new StringBuilder("RabbitMQListener.consumeResultFromReportService Message.body: ").append(message.getBody()).toString(), ex);
        } catch (Exception ex) {
            log.error(new StringBuilder("RabbitMQListener.consumeResultFromReportService Message.body: ").append(message.getBody()).toString(), ex);
        }
    }



}
