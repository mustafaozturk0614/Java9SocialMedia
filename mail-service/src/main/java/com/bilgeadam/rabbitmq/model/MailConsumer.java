package com.bilgeadam.rabbitmq.model;

import com.bilgeadam.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailConsumer {

    private final MailSenderService mailSenderService;

    @RabbitListener(queues = "${rabbitmq.mail-queue}")
    public void  sendMail(MailModel model){
        log.info("Model {}",model);
        mailSenderService.sendMail(model);
    }

}
