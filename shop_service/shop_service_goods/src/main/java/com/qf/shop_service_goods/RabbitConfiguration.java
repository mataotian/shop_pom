package com.qf.shop_service_goods;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    public static final String fanout_name="direct_exchange";
    @Bean
    public Queue getqueue1(){
        return new Queue("goods1_queue");
    }
    @Bean
    public Queue getqueue2(){
        return new Queue("goods2_queue");
    }
    @Bean
    public FanoutExchange getExchange(){
        return new FanoutExchange(fanout_name);
    }
    @Bean
    public Binding getbinding1(Queue getqueue1,FanoutExchange getExchange){
        return BindingBuilder.bind(getqueue1).to(getExchange);
    }
    @Bean
    public Binding getbinding2(Queue getqueue2,FanoutExchange getExchange){
        return BindingBuilder.bind(getqueue2).to(getExchange);
    }
}
