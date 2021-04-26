package ru.filippov.neat.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.filippov.neat.config.RabbitConfig;
import ru.filippov.neat.dto.ExperimentData;
import ru.filippov.neat.dto.services.preprocessing.NormalizationData;
import ru.filippov.neat.dto.services.preprocessing.VerificationData;

@Component
@RequiredArgsConstructor
public class RabbitMQWriter {

    @Autowired
    RabbitConfig rabbitConfig;

    private final RabbitTemplate rabbitTemplate;

    public void sendDataToPredict(ExperimentData data) throws JsonProcessingException {

        /*MessageProperties props = MessagePropertiesBuilder.newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
        Message message = MessageBuilder.withBody(data.toJson().getBytes())
                .andProperties(props)
                .build();*/

        rabbitTemplate.convertAndSend(rabbitConfig.RABBITMQ_OUTPUT_PREDICTION_SERVICE_EXCHANGE, rabbitConfig.RABBITMQ_OUTPUT_PREDICTION_SERVICE_ROUTING_KEY, data);

    }

    public void sendDataToNormalize(NormalizationData normalizationData){
        rabbitTemplate.convertAndSend(rabbitConfig.RABBITMQ_OUTPUT_NORMALIZE_DATA_EXCHANGE, rabbitConfig.RABBITMQ_OUTPUT_NORMALIZE_DATA_QUEUE, normalizationData);
    }

    public void sendDataToVerify(VerificationData verificationData){
        rabbitTemplate.convertAndSend(rabbitConfig.RABBITMQ_OUTPUT_NORMALIZE_DATA_EXCHANGE, rabbitConfig.RABBITMQ_OUTPUT_NORMALIZE_DATA_QUEUE, verificationData);
    }
}
