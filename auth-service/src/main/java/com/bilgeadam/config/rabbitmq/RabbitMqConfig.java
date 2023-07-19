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

        @Value("${rabbitmq.auth-exchange}")
        private String exchange;

        /////register
         @Value("${rabbitmq.register-queue}")
        private String registerQueueName;
         @Value("${rabbitmq.register-bindingKey}")
        private String registerBindingKey;
         ////activation
         @Value("${rabbitmq.activation-queue}")
         private String activationQueueName;
         @Value("${rabbitmq.activation-bindingKey}")
         private String activationBindingKey;
         // mail
         @Value("${rabbitmq.mail-queue}")
        private String mailQueueName;
        @Value("${rabbitmq.mail-bindingKey}")
        private String mailBindingKey;


        @Bean
         DirectExchange exchangeAuth(){
             return new DirectExchange(exchange);
         }

         @Bean
         Queue registerQueue(){
            return  new Queue(registerQueueName);
         }

         @Bean
       public Binding bindingRegister(final Queue  registerQueue,final DirectExchange exchangeAuth ){
                return BindingBuilder.bind(registerQueue).to(exchangeAuth).with(registerBindingKey);
         }


         /// activation
    @Bean
    Queue activationQueue(){
        return  new Queue(activationQueueName);
    }

    @Bean
    public Binding bindingActivation(final Queue  activationQueue,final DirectExchange exchangeAuth ){
        return BindingBuilder.bind(activationQueue).to(exchangeAuth).with(activationBindingKey);
    }


    /// mail

    @Bean
    Queue mailQueue(){
        return  new Queue(mailQueueName);
    }

    @Bean
    public Binding bindingMail(final Queue  mailQueue,final DirectExchange exchangeAuth ){
        return BindingBuilder.bind(mailQueue).to(exchangeAuth).with(mailBindingKey);
    }
}
