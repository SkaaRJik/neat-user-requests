package ru.filippov.neat.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.filippov.neat.config.RabbitConfig;
import ru.filippov.neat.dto.ExperimentDataForPredictionServiceDto;
import ru.filippov.neat.dto.services.preprocessing.NormalizationData;
import ru.filippov.neat.dto.services.preprocessing.VerificationData;

@Component
@RequiredArgsConstructor
public class RabbitMQWriter {

    @Autowired
    private RabbitConfig rabbitConfig;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendDataToPredict(ExperimentDataForPredictionServiceDto data) throws JsonProcessingException {
        String json = this.objectMapper.writeValueAsString(data);
        rabbitTemplate.convertAndSend(rabbitConfig.RABBITMQ_OUTPUT_PREDICTION_SERVICE_EXCHANGE, rabbitConfig.RABBITMQ_OUTPUT_PREDICTION_SERVICE_ROUTING_KEY, json);
    }

    public void sendDataToNormalize(NormalizationData normalizationData) throws JsonProcessingException {
        String json = this.objectMapper.writeValueAsString(normalizationData);
        rabbitTemplate.convertAndSend(rabbitConfig.RABBITMQ_OUTPUT_NORMALIZE_DATA_EXCHANGE, rabbitConfig.RABBITMQ_OUTPUT_NORMALIZE_DATA_QUEUE, json);
    }

    public void sendDataToVerify(VerificationData verificationData) throws JsonProcessingException {
        String json = this.objectMapper.writeValueAsString(verificationData);
        rabbitTemplate.convertAndSend(rabbitConfig.RABBITMQ_OUTPUT_VERIFICATE_DOCUMENT_EXCHANGE, rabbitConfig.RABBITMQ_OUTPUT_VERIFICATE_DOCUMENT_ROUTING_KEY, json);
    }
}
