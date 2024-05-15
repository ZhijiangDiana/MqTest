package com.mq_test.mq_test.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfiguration {

    // 使用Spring AMQP进行声明
    @Bean // 翻译业务交换机
    public DirectExchange oriExchange() {
        return new DirectExchange("translate.exchange", true, false, new HashMap<>());
    }

    @Bean // 翻译业务队列
    public Queue oriQueue() {
        Map<String, Object> map = new HashMap<>();
        return new Queue("translate.queue", true, false, false, map);
    }

    @Bean // 绑定原队列到原交换机
    public Binding bindingOriEx() {
        return BindingBuilder.bind(oriQueue()).to(oriExchange()).with("114514");
    }



    @Bean // 菜品识别业务交换机
    public DirectExchange mealRecoExchange() {
        return new DirectExchange("mealReco.exchange", true, false, new HashMap<>());
    }

    @Bean // 菜品识别业务队列
    public Queue mealRecoQueue() {
        Map<String, Object> map = new HashMap<>();
        return new Queue("mealReco.queue", true, false, false, map);
    }

    @Bean // 绑定
    public Binding bindingMealEx() {
        return BindingBuilder.bind(mealRecoQueue()).to(mealRecoExchange()).with("114514");
    }

}
