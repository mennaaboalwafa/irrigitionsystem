package com.example.irrigationsystem.service.impl;

import com.example.irrigationsystem.exception.AlertException;
import com.example.irrigationsystem.operation.SensorDeviceOperation;
import com.example.irrigationsystem.service.IrrigationSystemService;
import com.example.irrigationsystem.core.utillites.HeaderUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Service
public class IrrigationSystemImpl implements IrrigationSystemService {

    private final SensorDeviceOperation sensorDeviceOperation;

    public IrrigationSystemImpl(SensorDeviceOperation sensorDeviceOperation) {
        this.sensorDeviceOperation = sensorDeviceOperation;
    }

    @NonNull
    public Mono<ServerResponse> startIrrigation(ServerRequest serverRequest){
        return serverRequest.bodyToMono(String.class)
                .doOnNext(s -> log.info("Request Payload-\n{}", s))
                .flatMap(s -> sensorDeviceOperation.postToSensorDevice(s, HeaderUtils.httpToKafka(serverRequest)))
                .map(ConsumerRecord::value)
                .flatMap(v -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(v))
                .doOnError(throwable -> badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(new AlertException()));
    }
}
