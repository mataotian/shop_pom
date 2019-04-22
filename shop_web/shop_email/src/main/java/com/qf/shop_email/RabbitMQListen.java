package com.qf.shop_email;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQListen {
    @Bean
    public Queue getQueue(){
        return new Queue("email_queue");
    }
}
