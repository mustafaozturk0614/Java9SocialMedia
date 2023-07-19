package com.bilgeadam.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationProducer {

    private  final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.auth-exchange}")
   private  String directExchange;
    @Value("${rabbitmq.activation-bindingKey}")
   private  String activationBindingKey;

    public void activation(Long authId){

        rabbitTemplate.convertAndSend(directExchange,activationBindingKey,authId);
    }


}
