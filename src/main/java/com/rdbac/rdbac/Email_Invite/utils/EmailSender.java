package com.rdbac.rdbac.Email_Invite.utils;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdbac.rdbac.Email_Invite.application.dtop.EmailQueueDto;
import com.rdbac.rdbac.Email_Invite.domain.model.Email_Invitation;
import com.rdbac.rdbac.exceptions.EmailSendException;
import com.rdbac.rdbac.rabbitmq.config.ExchangeConfig;
import com.rdbac.rdbac.rabbitmq.config.QueuesConfig;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailSender {
   
    private  JavaMailSender javaMailSender ;
    private TemplateEngine templateEngine;
    private ObjectMapper objectMapper;
    
    @Value("${REACT_APP_URL}")
    private String reactAppUri;

    private static int count = 0;
    public EmailSender(JavaMailSender javaMailSender,
                        TemplateEngine templateEngine,
                        ObjectMapper objectMapper
    ) {
        this.javaMailSender = javaMailSender;
        this.templateEngine =templateEngine;
        this.objectMapper = objectMapper;
        log.info(reactAppUri);
    }

    @RabbitListener(queues = QueuesConfig.EMAIL_QUEUE)
    public void send_Message(String emailQueueDtoJson) throws JsonMappingException, JsonProcessingException  {
         long start = System.currentTimeMillis();

         log.info("Recived the Email for this {} and that is the Async Task about to be send " , emailQueueDtoJson);
         EmailQueueDto emailQueueDto = objectMapper.readValue(emailQueueDtoJson, EmailQueueDto.class);
         Email_Invitation email_Invitation = emailQueueDto.getEmailInvitation();
         String token = emailQueueDto.getToken();

        log.info("[{}] Starting email send to {} count is {}", Thread.currentThread().getName(), email_Invitation.getUserRecivedEmail(),count++);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
             mimeMessageHelper.setSubject("Invite to AccesVolt"); 
            mimeMessageHelper.addTo(email_Invitation.getUserRecivedEmail());
           
            //precess the thymleaf
            Context context = new Context();
            context.setVariable("sent_user", email_Invitation.getUser_sent_email());


            // React App Url. about to be. Publish  in the app -config 
            context.setVariable("verificationLink", reactAppUri+"/email/accept/"+token);

            String htmlContent = templateEngine.process("email-template", context);

         //   log.info( email_Invitation.toString() + " " + mimeMessage.toString());

            mimeMessageHelper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            long duration = System.currentTimeMillis() - start;
        log.info("[{}] Email sent to {} in {} ms", Thread.currentThread().getName(), email_Invitation.getUserRecivedEmail(), duration);

        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send email", e);
        }
        catch(Exception e) {
             throw new EmailSendException("Unexpected email send failure", e);
       }
    }
}
