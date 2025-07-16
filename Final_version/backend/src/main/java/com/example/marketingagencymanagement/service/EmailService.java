package com.example.marketingagencymanagement.service;

import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;
    private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    @Async
    public void sendNotificaitionAsync(String email, String title, String message) {

        //Thread.sleep(5000);
        //System.out.println("Slanje emaila...");

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setTo(email);
            helper.setFrom(env.getProperty("spring.mail.username"));
            System.out.println(env.getProperty("spring.mail.username"));
            helper.setSubject(title);

            helper.setText(message, true);
            javaMailSender.send(mimeMessage);
            logger.info(env.getProperty("spring.mail.username")+" je poslao mejl korisnku s mejlom "+email+"!");
            //System.out.println("Email poslat!");

        } catch (MessagingException e) {
            logger.error("Korisnku "+env.getProperty("spring.mail.username")+" mejl nije poslat!");
            throw new MailException("Failed to send email", e) {
            };
        }
    }
}
