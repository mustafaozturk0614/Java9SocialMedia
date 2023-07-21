package com.bilgeadam.rabbitmq.producer;

import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterElasticProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.register-elastic-binding}")
    private String registerElasticBindig;

    @Value("${rabbitmq.user-exchange}")
    private String userExchange;

    public void  sendNewUser(RegisterElasticModel model){
        rabbitTemplate.convertAndSend(userExchange,registerElasticBindig,model);
    }

}
