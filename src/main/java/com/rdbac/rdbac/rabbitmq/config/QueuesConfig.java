package com.rdbac.rdbac.rabbitmq.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class QueuesConfig {

     public static final String EMAIL_QUEUE = "email.queue";
   // static final String NOTIFICATION_QUEUE = "order.notification.queue";
    static final String EMAIL_DLQ = "email.dlq";
    //static final String NOTIFICATION_DLQ = "order.notification.dlq";
    static final Integer DLQ_TTL = 1000*60*5;

    public static final String AUDIT_QUEUE = "audit.queue";
    public static final String AUDIT_DLQ = "audit.dlq";



    @Bean(name="email.queue")
    public Queue Email_Queue() {
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", ExchangeConfig.EMAIL_DLQ_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "email.dlq"); // this is now hard coded but need to be changes in that is imporamt 
        arguments.put("x-message-ttl", DLQ_TTL); 


        return QueueBuilder.durable(EMAIL_QUEUE).withArguments(arguments).build();
    }

    @Bean(name="email.dlq")
    public Queue Email_Dlq_queue() {
        return QueueBuilder.durable(EMAIL_DLQ).build();
    }

    @Bean(name = "audit.queue" )
    public Queue auditQueue()   {
         Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", ExchangeConfig.AUDIT_DLQ_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "audit.dlq"); // this is now hard coded but need to be changes in that is imporamt 
        arguments.put("x-message-ttl", DLQ_TTL); 

        return QueueBuilder.durable(AUDIT_QUEUE).withArguments(arguments).build();
    }

    @Bean(name="audit.dlq")
    public Queue Audit_Dlq_queue() {
        return QueueBuilder.durable(AUDIT_DLQ).build();
    }
}
