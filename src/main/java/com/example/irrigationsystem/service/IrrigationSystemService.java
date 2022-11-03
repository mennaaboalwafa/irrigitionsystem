package com.example.irrigationsystem.service;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface IrrigationSystemService {
    Mono<ServerResponse> startIrrigation(ServerRequest serverRequest);
}
