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


    @Value("${rabbitmq.register-elastic-queue}")
    private String registerElasticQueueName;

    @Value("${rabbitmq.register-elastic-binding}")
    private String registerElasticBindig;

    @Value("${rabbitmq.user-exchange}")
    private String userExchange;


    @Bean
    Queue registerElasticQueue(){
        return  new Queue(registerElasticQueueName);
    }
    @Bean
    DirectExchange exchange(){
        return new DirectExchange(userExchange);
    }

    @Bean
    public  Binding bindingRegisterElastic(final Queue registerElasticQueue,DirectExchange exchange){

        return BindingBuilder.bind(registerElasticQueue).to(exchange).with(registerElasticBindig);
    }




    @Bean
    Queue activationQueue(){
        return  new Queue(activationQueueName);
    }

         @Bean
         Queue registerQueue(){
            return  new Queue(registerQueueName);
         }



}
