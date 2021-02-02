package ru.filippov.neat.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.filippov.neat.dto.services.prediction.ExperimentStatusDto;
import ru.filippov.neat.dto.services.prediction.PredictionServiceResult;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Data
public class RabbitConfig {

    @Value("${rabbitmq.input.exchange:experiment-service}")
    public String RABBITMQ_OUTPUT_EXCHANGE;
    @Value("${rabbitmq.input.queue.experiment.experiment:experiment}")
    public String RABBITMQ_OUTPUT_QUEUE;
    @Value("${rabbitmq.input.routingKeys.data:data}")
    public String RABBITMQ_OUTPUT_DATA_ROUTING_KEY;

    @Value("${rabbitmq.output.exchange:user-queries-service}")
    public String RABBITMQ_INPUT_EXCHANGE;
    @Value("${rabbitmq.output.queue.result:result}")
    public String RABBITMQ_INPUT_RESULT_QUEUE;
    @Value("${rabbitmq.output.queue.status:status}")
    public String RABBITMQ_INPUT_STATUS_QUEUE;
    @Value("${rabbitmq.output.routingKeys.result:result}")
    public String RABBITMQ_INPUT_RESULT_ROUTING_KEY;
    @Value("${rabbitmq.output.routingKeys.status:status}")
    public String RABBITMQ_INPUT_STATUS_ROUTING_KEY;


    @Bean
    public TopicExchange rabbitmqOutputExchange() {
        return ExchangeBuilder.topicExchange(RABBITMQ_OUTPUT_EXCHANGE).build();
    }

    @Bean
    public Queue rabbitmqOutputQueue() {
        return QueueBuilder.durable(RABBITMQ_OUTPUT_QUEUE).build();
    }

    @Bean
    public Binding rabbitMqOutputBinding(Queue rabbitmqOutputQueue, TopicExchange rabbitmqOutputExchange) {
        return BindingBuilder.bind(rabbitmqOutputQueue).to(rabbitmqOutputExchange).with(RABBITMQ_OUTPUT_DATA_ROUTING_KEY);
    }



    @Bean
    public TopicExchange rabbitmqInputExchange() {
        return ExchangeBuilder.topicExchange(RABBITMQ_INPUT_EXCHANGE).build();
    }

    @Bean
    public Queue rabbitmqResultInputQueue() {
        return QueueBuilder.durable(RABBITMQ_INPUT_RESULT_QUEUE).build();
    }

    @Bean
    public Binding rabbitMqResultInputBinding(Queue rabbitmqResultInputQueue, TopicExchange rabbitmqInputExchange) {
        return BindingBuilder.bind(rabbitmqResultInputQueue).to(rabbitmqInputExchange).with(RABBITMQ_INPUT_RESULT_ROUTING_KEY);
    }


    @Bean
    public Queue rabbitmqStatusInputQueue() {
        return QueueBuilder.durable(RABBITMQ_INPUT_STATUS_QUEUE).build();
    }

    @Bean
    public Binding rabbitMqStatusInputBinding(Queue rabbitmqStatusInputQueue, TopicExchange rabbitmqInputExchange) {
        return BindingBuilder.bind(rabbitmqStatusInputQueue).to(rabbitmqInputExchange).with(RABBITMQ_INPUT_STATUS_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory, final Jackson2JsonMessageConverter messageConverter){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }



    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        return objectMapper;
    }

    @Bean
    public Jackson2JsonMessageConverter converter(final ObjectMapper objectMapper) {
        final Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter(objectMapper);
        jackson2JsonMessageConverter.setClassMapper(classMapper());
        return jackson2JsonMessageConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        classMapper.setTrustedPackages("ru.filippov.neatexecutor.entity.*");
        //classMapper.setTrustedPackages("ru.filippov.neat.dto", "ru.filippov.neat.dto.services");
        idClassMapping.put("ru.filippov.neatexecutor.entity.ExperimentStatusDto", ExperimentStatusDto.class);
        idClassMapping.put("ru.filippov.neatexecutor.entity.ServiceResult", PredictionServiceResult.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }


}
