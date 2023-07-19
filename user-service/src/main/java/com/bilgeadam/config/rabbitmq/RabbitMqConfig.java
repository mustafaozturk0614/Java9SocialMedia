package com.bilgeadam.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

         @Value("${rabbitmq.register-queue}")
        private String registerQueueName;

    @Value("${rabbitmq.activation-queue}")
    private String activationQueueName;
    @Bean
    Queue activationQueue(){
        return  new Queue(activationQueueName);
    }

         @Bean
         Queue registerQueue(){
            return  new Queue(registerQueueName);
         }



}
