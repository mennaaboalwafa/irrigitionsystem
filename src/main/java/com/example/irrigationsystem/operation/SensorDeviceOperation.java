package com.example.irrigationsystem.operation;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
public class SensorDeviceOperation {
    @Resource
    private  ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate;

    @Value("${application.sensor-device.request.topic}")
    private String requestTopic;

    @Value("${application.sensor-device.response.topic}")
    private String requestReplyTopic;

    @Value("${application.sensor-device.reply-timeout:30s}")
    private Duration sensorDeviceTimeout;

    public SensorDeviceOperation( ) {

    }

    public Mono<ConsumerRecord<String,String>> postToSensorDevice(String value, Map<String, String> headers) {
        ProducerRecord<String,String> record= new ProducerRecord<>(requestTopic,value);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC,requestReplyTopic.getBytes()));
        if (headers!=null){
            headers.entrySet().stream()
                    .filter(entry -> entry.getKey()!=null && entry.getValue()!=null)
                    .forEach(entry-> record.headers().add(entry.getKey(),entry.getValue().getBytes()));
        }

        RequestReplyFuture<String,String,String> sendReceive=replyKafkaTemplate.sendAndReceive(record,sensorDeviceTimeout);

        log.info("post to sensor device -\n{}",record);
        return Mono.fromFuture(sendReceive::completable);
    }
}
