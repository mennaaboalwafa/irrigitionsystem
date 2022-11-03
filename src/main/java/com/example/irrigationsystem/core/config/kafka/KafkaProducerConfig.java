package com.example.irrigationsystem.core.config.kafka;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.GenericMessageListenerContainer;
import org.springframework.kafka.requestreply.CorrelationKey;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaProducerConfig {

   private final KafkaConfig kafkaConfig;

    @Value("${application.kafka.reply.timeout:20s}")
    private Duration replyTimeout;

    @Value("${application.sensor-device.response.topic}")
    private String requestReplyTopic;

    public KafkaProducerConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }


    @Bean
    public KafkaTemplate<String,String> kafkaOperation(){
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaConfig.producerConfig());
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate(ProducerFactory<String, String> producerFactory) {
        ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate =
                new ReplyingKafkaTemplate<>(producerFactory, replyContainer());

        replyKafkaTemplate.setDefaultReplyTimeout(replyTimeout);
        replyKafkaTemplate.setCorrelationIdStrategy(stringStringProducerRecord -> new CorrelationKey(randomUUIDAsBytes()));

        return replyKafkaTemplate;
    }

    static byte[] randomUUIDAsBytes(){
       return UUID.randomUUID().toString().getBytes();
    }


    private GenericMessageListenerContainer<String,String> replyContainer() {
        ContainerProperties containerProperties=new ContainerProperties(requestReplyTopic);
        return new ConcurrentMessageListenerContainer<>(syncConsumerFactory(),containerProperties);

    }

    private ConsumerFactory<String,String> syncConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(syncConsumerConfiguration());
    }

    private Map<String,Object> syncConsumerConfiguration() {
        Map<String,Object> properties=kafkaConfig.consumerConfig();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,groupId());
        return properties;
    }

    private String groupId(){
        String definedSyncGroup=kafkaConfig.getSyncGroupId();
        StringUtils.hasText(definedSyncGroup);
        return definedSyncGroup;
    }
}