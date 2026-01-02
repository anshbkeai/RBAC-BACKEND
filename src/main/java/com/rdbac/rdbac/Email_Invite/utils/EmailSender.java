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
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

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
    private final SendGrid sendGrid;

    @Value("${REACT_APP_URL}")
    private String reactAppUri;

    @Value("${SENDGRID_FROM_EMAIL}")
    private String fromEmail;


    private static int count = 0;
    public EmailSender(JavaMailSender javaMailSender,
                        TemplateEngine templateEngine,
                        ObjectMapper objectMapper,
                        @Value("${SEND_GRID_API_KEY}") String sendGridApi
                        
    ) {
        this.javaMailSender = javaMailSender;
        this.templateEngine =templateEngine;
        this.objectMapper = objectMapper;
        this.sendGrid = new SendGrid(sendGridApi);
        log.info(sendGrid.toString());
    }

    @RabbitListener(queues = QueuesConfig.EMAIL_QUEUE)
    public void send_Message(String emailQueueDtoJson) throws JsonMappingException, JsonProcessingException  {
         long start = System.currentTimeMillis();

         log.info("Recived the Email for this {} and that is the Async Task about to be send " , emailQueueDtoJson);
         EmailQueueDto emailQueueDto = objectMapper.readValue(emailQueueDtoJson, EmailQueueDto.class);
         Email_Invitation emailInvitation = emailQueueDto.getEmailInvitation();
         String token = emailQueueDto.getToken();

        log.info("[{}] Starting email send to {} count is {}", Thread.currentThread().getName(), emailInvitation.getUserRecivedEmail(),count++);

        
         try {
            // Thymeleaf processing
            Context context = new Context();
            context.setVariable("sent_user", emailInvitation.getUser_sent_email());
            context.setVariable(
                    "verificationLink",
                    reactAppUri + "/email/accept/" + token
            );

            String htmlContent =
                    templateEngine.process("email-template", context);

            // SendGrid email
            Email from = new Email(fromEmail);
            Email to = new Email(emailInvitation.getUserRecivedEmail());
            Content content = new Content("text/html", htmlContent);
            Mail mail = new Mail(from, "Invite to AccesVolt", to, content);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            if (response.getStatusCode() >= 400) {
                throw new EmailSendException(
                        "SendGrid failed: " + response.getBody()
                );
            }

            long duration = System.currentTimeMillis() - start;
            log.info(
                    "[{}] Email sent to {} in {} ms",
                    Thread.currentThread().getName(),
                    emailInvitation.getUserRecivedEmail(),
                    duration
            );

        } catch (Exception e) {
            throw new EmailSendException("Failed to send email via SendGrid", e);
        }
    }
}
