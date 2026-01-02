package com.rdbac.rdbac.rabbitmq.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {

     static final String EMAIL_EXCHANGE = "email.exchange";
     static final String EMAIL_DLQ_EXCHANGE = "email.dlq.exchange";

     static final String AUDIT_EXCHANGE = "audit.exchange";
     static final String AUDIT_DLQ_EXCHANGE = "audit.dlq.exchange";

    //if we can to create about 

    @Bean(name = "email.exchange")
    public TopicExchange email_topic_exhange() {
       
        return new TopicExchange(EMAIL_EXCHANGE, true, false);
    }

    @Bean(name="email.dlq.exchange")
    public DirectExchange email_dlq_directExchange() {
        return new DirectExchange(EMAIL_DLQ_EXCHANGE, true, false);
    }

    @Bean(name = "audit.exchange")
    public TopicExchange aduitExchange() {
        return new TopicExchange(AUDIT_EXCHANGE, true, false);
    }

    @Bean(name= "audit.dlq.exchange")
    public DirectExchange audit_dlq_directExchange() {
        return new DirectExchange(AUDIT_DLQ_EXCHANGE, true, false);
    }

 // RabbitMq  tell me us about that we have different about like topic, Fannout and that /
 // so in this we are using about the Topic Exchnange 

    
}
