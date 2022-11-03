package com.example.irrigationsystem.core.config.kafka;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

   private final KafkaConfig kafkaConfig;


    public KafkaConsumerConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }



    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,String> factory=new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfiguration());
    }

    public Map<String,Object> consumerConfiguration() {
       return kafkaConfig.consumerConfig();
    }

    @Bean
    public CommonErrorHandler errorHandler(){return  new CommonLoggingErrorHandler();}

}