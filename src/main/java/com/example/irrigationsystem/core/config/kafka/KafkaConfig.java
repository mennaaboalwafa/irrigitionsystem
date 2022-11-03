package com.example.irrigationsystem.core.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "application.kafka")
public class KafkaConfig {
    @Value("${application.kafka.bootstrap.server}")
    private String bootstrapServer;

    @Value("${application.kafka.sync.group.id:@null}")
    private String syncGroupId;

    private Map<String,String> consumer;
    private Map<String,String> producer;



    public void setBootstrapServer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }


    public void setConsumer(Map<String, String> consumer) {
        this.consumer = consumer;
    }



    public void setProducer(Map<String, String> producer) {
        this.producer = producer;
    }

    public void setSyncGroupId(String syncGroupId) {
        this.syncGroupId = syncGroupId;
    }

    public String getSyncGroupId() {
        return syncGroupId;
    }

    public Map<String, Object>  producerConfig(){
        Map<String,Object> config=new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        if (!CollectionUtils.isEmpty(producer)){
            config.putAll(producer);
        }
        return config;
    }


    public String getConsumerGroupId(){
        if ((!CollectionUtils.isEmpty(consumer))){
            return consumer.get(ConsumerConfig.GROUP_ID_CONFIG);
        }else {
            return "";
        }
    }

    public Map<String,Object> consumerConfig(){
        Map<String,Object> config=new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class);
        if (!CollectionUtils.isEmpty(consumer)){
            config.putAll(consumer);
        }
        return config;
    }




}
