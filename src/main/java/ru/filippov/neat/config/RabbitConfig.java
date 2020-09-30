package ru.filippov.neat.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class RabbitConfig {

    @Value("${rabbitmq.output.exchange:experiment-server}")
    public String RABBITMQ_OUTPUT_EXPERIMENT_SERVER_EXCHANGE;
    @Value("${rabbitmq.output.queue:experiment}")
    public String RABBITMQ_OUTPUT_EXPERIMENT_SERVER_QUEUE;
    @Value("${rabbitmq.output.routingkey:data}")
    public String RABBITMQ_OUTPUT_EXPERIMENT_SERVER_ROUTING_KEY;


    @Bean
    public Queue rabbitmqInputQueue() {
        return QueueBuilder.durable(RABBITMQ_OUTPUT_EXPERIMENT_SERVER_QUEUE).build();
    }

    @Bean
    public TopicExchange rabbitmqInputExchange() {
        return (TopicExchange) ExchangeBuilder.topicExchange(RABBITMQ_OUTPUT_EXPERIMENT_SERVER_EXCHANGE).build();
    }

    @Bean
    public Binding binding(Queue rabbitmqInputQueue, TopicExchange rabbitmqInputExchange) {
        return BindingBuilder.bind(rabbitmqInputQueue).to(rabbitmqInputExchange).with(RABBITMQ_OUTPUT_EXPERIMENT_SERVER_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }


}
