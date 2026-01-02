package com.rdbac.rdbac.audit.infrastructure.utils;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdbac.rdbac.audit.domain.model.AuditEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AudtiEventPublisher {

    
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    private final TopicExchange topicExchange;

    public AudtiEventPublisher(
        @Qualifier("audit.exchange") TopicExchange topicExchange,
        ObjectMapper objectMapper,
        RabbitTemplate rabbitTemplate
    ) {
        this.topicExchange = topicExchange;
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void auditPublish(AuditEvent auditEvent) {
        try {
            String audit = objectMapper.writeValueAsString(auditEvent);
            rabbitTemplate.convertAndSend(topicExchange.getName(), "audit.publish", audit);
            log.info("Audit Sent Publish to exchamge {} routing key audit.sent"  , topicExchange.getName());

        } catch (Exception e) {
            e.getMessage();
        }
    }

}
