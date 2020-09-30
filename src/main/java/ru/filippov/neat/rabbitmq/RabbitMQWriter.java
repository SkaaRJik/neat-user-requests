package ru.filippov.neat.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.filippov.neat.dto.ExperimentData;

@Component
@RequiredArgsConstructor
public class RabbitMQWriter {

    @Value("${rabbitmq.output.exchange:experiment-server}")
    public String RABBITMQ_OUTPUT_EXPERIMENT_SERVER_EXCHANGE;
    @Value("${rabbitmq.output.queue:experiment}")
    public String RABBITMQ_OUTPUT_EXPERIMENT_SERVER_QUEUE;
    @Value("${rabbitmq.output.routingkey:data}")
    public String RABBITMQ_OUTPUT_EXPERIMENT_SERVER_ROUTING_KEY;

    private final RabbitTemplate rabbitTemplate;

    public void writeIntoExperimentServerQueue(ExperimentData data) throws JsonProcessingException {

        /*MessageProperties props = MessagePropertiesBuilder.newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
        Message message = MessageBuilder.withBody(data.toJson().getBytes())
                .andProperties(props)
                .build();*/

        rabbitTemplate.convertAndSend(RABBITMQ_OUTPUT_EXPERIMENT_SERVER_EXCHANGE, RABBITMQ_OUTPUT_EXPERIMENT_SERVER_ROUTING_KEY, data);

    }
}
