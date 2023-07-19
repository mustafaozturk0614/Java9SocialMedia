package com.bilgeadam.service;

import com.bilgeadam.rabbitmq.model.MailModel;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender javaMailSender;


    public void sendMail(MailModel model){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom("${java9mail}");
        mailMessage.setTo(model.getEmail());
        mailMessage.setSubject("AKTİVASYON KODUNUZ....");
        mailMessage.setText("code: "+ model.getActivationCode());
        javaMailSender.send(mailMessage);
    }

}
