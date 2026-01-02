package com.rdbac.rdbac.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BindingConfig {

    @Bean
    public Binding email_binding(
      @Qualifier(ExchangeConfig.EMAIL_EXCHANGE)  TopicExchange topicExchange,
        @Qualifier(QueuesConfig.EMAIL_QUEUE) Queue queue
    ) {
        return BindingBuilder.bind(queue).to(topicExchange).with("email.sent"); // shoudl be ghe ENUMS getting values.  // thses are hardcoded 
    }

     @Bean
    public Binding email_binding_dlq(
      @Qualifier("email.dlq.exchange")  DirectExchange directExchange,
        @Qualifier("email.dlq") Queue queue
    ) {
        return BindingBuilder.bind(queue).to(directExchange).with("email.dlq"); 
    }


     @Bean
    public Binding auditDinding(
      @Qualifier("audit.exchange")  TopicExchange topicExchange,
        @Qualifier("audit.queue") Queue queue
    ) {
        return BindingBuilder.bind(queue).to(topicExchange).with("audit.publish"); // shoudl be ghe ENUMS getting values.  // thses are hardcoded 
    }


     @Bean
    public Binding audit_binding_dlq(
      @Qualifier("audit.dlq.exchange")  DirectExchange directExchange,
        @Qualifier("audit.dlq") Queue queue
    ) {
        return BindingBuilder.bind(queue).to(directExchange).with("audit.dlq"); 
    }

}
