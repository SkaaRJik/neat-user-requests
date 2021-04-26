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

    @Value("${rabbitmq.input.predictionResult.exchange:user-queries-service}")
    public String RABBITMQ_INPUT_PREDICTION_RESULT_EXCHANGE;
    @Value("${rabbitmq.input.predictionResult.queue:prediction-result}")
    public String RABBITMQ_INPUT_PREDICTION_RESULT_QUEUE;
    @Value("${rabbitmq.input.predictionResult.routingKey:prediction-result}")
    public String RABBITMQ_INPUT_PREDICTION_RESULT_ROUTING_KEY;

    @Bean
    public DirectExchange rabbitmqInputPredictionResultExchange() {
        return ExchangeBuilder.directExchange(RABBITMQ_INPUT_PREDICTION_RESULT_EXCHANGE).build();
    }

    @Bean
    public Queue rabbitmqInputPredictionResultQueue() {
        return QueueBuilder.durable(RABBITMQ_INPUT_PREDICTION_RESULT_QUEUE).build();
    }

    @Bean
    public Binding rabbitmqInputPredictionResultBinding(Queue rabbitmqInputPredictionResultQueue, DirectExchange rabbitmqInputPredictionResultExchange) {
        return BindingBuilder.bind(rabbitmqInputPredictionResultQueue).to(rabbitmqInputPredictionResultExchange).with(RABBITMQ_INPUT_PREDICTION_RESULT_ROUTING_KEY);
    }


    @Value("${rabbitmq.input.predictionStatus.exchange:user-queries-service}")
    public String RABBITMQ_INPUT_PREDICTION_STATUS_EXCHANGE;
    @Value("${rabbitmq.input.predictionStatus.queue:prediction-status}")
    public String RABBITMQ_INPUT_PREDICTION_STATUS_QUEUE;
    @Value("${rabbitmq.input.predictionStatus.routingKey:prediction-status}")
    public String RABBITMQ_INPUT_PREDICTION_STATUS_ROUTING_KEY;

    @Bean
    public DirectExchange rabbitmqInputPredictionStatusExchange() {
        return ExchangeBuilder.directExchange(RABBITMQ_INPUT_PREDICTION_STATUS_EXCHANGE).build();
    }

    @Bean
    public Queue rabbitmqInputPredictionStatusQueue() {
        return QueueBuilder.durable(RABBITMQ_INPUT_PREDICTION_STATUS_QUEUE).build();
    }

    @Bean
    public Binding rabbitmqInputPredictionStatusBinding(Queue rabbitmqInputPredictionStatusQueue, DirectExchange rabbitmqInputPredictionStatusExchange) {
        return BindingBuilder.bind(rabbitmqInputPredictionStatusQueue).to(rabbitmqInputPredictionStatusExchange).with(RABBITMQ_INPUT_PREDICTION_STATUS_ROUTING_KEY);
    }


    @Value("${rabbitmq.input.verificationResult.exchange:user-queries-service}")
    public String RABBITMQ_INPUT_VERIFICATION_RESULT_EXCHANGE;
    @Value("${rabbitmq.input.verificationResult.queue:verification-result}")
    public String RABBITMQ_INPUT_VERIFICATION_RESULT_QUEUE;
    @Value("${rabbitmq.input.verificationResult.routingKey:verification-result}")
    public String RABBITMQ_INPUT_VERIFICATION_RESULT_ROUTING_KEY;


    @Bean
    public DirectExchange rabbitmqInputVerificationResultExchange() {
        return ExchangeBuilder.directExchange(RABBITMQ_INPUT_VERIFICATION_RESULT_EXCHANGE).build();
    }

    @Bean
    public Queue rabbitmqInputVerificationResultQueue() {
        return QueueBuilder.durable(RABBITMQ_INPUT_VERIFICATION_RESULT_QUEUE).build();
    }

    @Bean
    public Binding rabbitmqInputVerificationResultBinding(Queue rabbitmqInputVerificationResultQueue, DirectExchange rabbitmqInputVerificationResultExchange) {
        return BindingBuilder.bind(rabbitmqInputVerificationResultQueue).to(rabbitmqInputVerificationResultExchange).with(RABBITMQ_INPUT_VERIFICATION_RESULT_ROUTING_KEY);
    }

    @Value("${rabbitmq.input.normalizationResult.exchange:user-queries-service}")
    public String RABBITMQ_INPUT_NORMALIZATION_RESULT_EXCHANGE;
    @Value("${rabbitmq.input.normalizationResult.queue:normalization-result}")
    public String RABBITMQ_INPUT_NORMALIZATION_RESULT_QUEUE;
    @Value("${rabbitmq.input.normalizationResult.routingKey:normalization-result}")
    public String RABBITMQ_INPUT_NORMALIZATION_RESULT_ROUTING_KEY;


    @Bean
    public DirectExchange rabbitmqInputNormalizationResultExchange() {
        return ExchangeBuilder.directExchange(RABBITMQ_INPUT_NORMALIZATION_RESULT_EXCHANGE).build();
    }

    @Bean
    public Queue rabbitmqInputNormalizationResultQueue() {
        return QueueBuilder.durable(RABBITMQ_INPUT_NORMALIZATION_RESULT_QUEUE).build();
    }

    @Bean
    public Binding rabbitmqInputNormalizationResultBinding(Queue rabbitmqInputNormalizationResultQueue, DirectExchange rabbitmqInputNormalizationResultExchange) {
        return BindingBuilder.bind(rabbitmqInputNormalizationResultQueue).to(rabbitmqInputNormalizationResultExchange).with(RABBITMQ_INPUT_NORMALIZATION_RESULT_ROUTING_KEY);
    }

    @Value("${rabbitmq.output.predictionService.exchange:prediction-service}")
    public String RABBITMQ_OUTPUT_PREDICTION_SERVICE_EXCHANGE;
    @Value("${rabbitmq.output.predictionService.queue:prediction-data}")
    public String RABBITMQ_OUTPUT_PREDICTION_SERVICE_QUEUE;
    @Value("${rabbitmq.output.predictionService.routingKey:prediction-data}")
    public String RABBITMQ_OUTPUT_PREDICTION_SERVICE_ROUTING_KEY;


    @Bean
    public DirectExchange rabbitmqOutputPredictionServiceExchange() {
        return ExchangeBuilder.directExchange(RABBITMQ_OUTPUT_PREDICTION_SERVICE_EXCHANGE).build();
    }

    @Bean
    public Queue rabbitmqOutputPredictionServiceQueue() {
        return QueueBuilder.durable(RABBITMQ_OUTPUT_PREDICTION_SERVICE_QUEUE).build();
    }

    @Bean
    public Binding rabbitmqOutputPredictionServiceBinding(Queue rabbitmqOutputPredictionServiceQueue, DirectExchange rabbitmqOutputPredictionServiceExchange) {
        return BindingBuilder.bind(rabbitmqOutputPredictionServiceQueue).to(rabbitmqOutputPredictionServiceExchange).with(RABBITMQ_OUTPUT_PREDICTION_SERVICE_ROUTING_KEY);
    }

    @Value("${rabbitmq.output.verificateDocument.exchange:data-preprocessing-service}")
    public String RABBITMQ_OUTPUT_VERIFICATE_DOCUMENT_EXCHANGE;
    @Value("${rabbitmq.output.verificateDocument.queue:verification-data}")
    public String RABBITMQ_OUTPUT_VERIFICATE_DOCUMENT_QUEUE;
    @Value("${rabbitmq.output.verificateDocument.routingKey:verification-data}")
    public String RABBITMQ_OUTPUT_VERIFICATE_DOCUMENT_ROUTING_KEY;


    @Bean
    public DirectExchange rabbitmqOutputVerificateDocumentExchange() {
        return ExchangeBuilder.directExchange(RABBITMQ_OUTPUT_VERIFICATE_DOCUMENT_EXCHANGE).build();
    }

    @Bean
    public Queue rabbitmqOutputVerificateDocumentQueue() {
        return QueueBuilder.durable(RABBITMQ_OUTPUT_VERIFICATE_DOCUMENT_QUEUE).build();
    }

    @Bean
    public Binding rabbitmqOutputVerificateDocumentBinding(Queue rabbitmqOutputVerificateDocumentQueue, DirectExchange rabbitmqOutputVerificateDocumentExchange) {
        return BindingBuilder.bind(rabbitmqOutputVerificateDocumentQueue).to(rabbitmqOutputVerificateDocumentExchange).with(RABBITMQ_OUTPUT_VERIFICATE_DOCUMENT_ROUTING_KEY);
    }

    @Value("${rabbitmq.output.normalizeData.exchange:data-preprocessing-service}")
    public String RABBITMQ_OUTPUT_NORMALIZE_DATA_EXCHANGE;
    @Value("${rabbitmq.output.normalizeData.queue:normalization-data}")
    public String RABBITMQ_OUTPUT_NORMALIZE_DATA_QUEUE;
    @Value("${rabbitmq.output.normalizeData.routingKey:normalization-data}")
    public String RABBITMQ_OUTPUT_NORMALIZE_DATA_ROUTING_KEY;


    @Bean
    public DirectExchange rabbitmqOutputNormalizeDataExchange() {
        return ExchangeBuilder.directExchange(RABBITMQ_OUTPUT_NORMALIZE_DATA_EXCHANGE).build();
    }

    @Bean
    public Queue rabbitmqOutputNormalizeDataQueue() {
        return QueueBuilder.durable(RABBITMQ_OUTPUT_NORMALIZE_DATA_QUEUE).build();
    }

    @Bean
    public Binding rabbitmqOutputNormalizeDataBinding(Queue rabbitmqOutputNormalizeDataQueue, DirectExchange rabbitmqOutputNormalizeDataExchange) {
        return BindingBuilder.bind(rabbitmqOutputNormalizeDataQueue).to(rabbitmqOutputNormalizeDataExchange).with(RABBITMQ_OUTPUT_NORMALIZE_DATA_ROUTING_KEY);
    }
    
   /* @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory, final Jackson2JsonMessageConverter messageConverter){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }*/



    /*@Bean
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
        idClassMapping.put("ru.filippov.neat.dto", PredictionServiceResult.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }*/


}
