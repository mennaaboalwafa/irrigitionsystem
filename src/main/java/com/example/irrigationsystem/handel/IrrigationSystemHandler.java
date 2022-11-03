package com.example.irrigationsystem.handel;

import com.example.irrigationsystem.exception.AlertException;
import com.example.irrigationsystem.exception.GeneralException;
import com.example.irrigationsystem.dto.Land;
import com.example.irrigationsystem.dto.LandConfiguration;
import com.example.irrigationsystem.repository.LandConfigurationRepository;
import com.example.irrigationsystem.repository.LandRepository;
import com.example.irrigationsystem.service.IrrigationSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Consumer;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
public class IrrigationSystemHandler{

    private final IrrigationSystemService irrigationSystemService;
    private final LandRepository landRepository;

    private final LandConfigurationRepository landConfigurationRepository;

    public IrrigationSystemHandler(IrrigationSystemService irrigationSystemService, LandRepository landRepository, LandConfigurationRepository landConfigurationRepository) {
        this.irrigationSystemService = irrigationSystemService;
        this.landRepository = landRepository;
        this.landConfigurationRepository = landConfigurationRepository;
    }

    public Mono<ServerResponse> startIrrigation(ServerRequest serverRequest) {
        String landId= serverRequest.pathVariable("landId");
        log.info("Request Received landId= {} ",landId);
       Optional<Land> land = landRepository.findById(1L);
        if (land.isPresent()) {
            if (land.get().isIrrigated())
                return Mono.error(new GeneralException(HttpStatus.BAD_REQUEST, "400", "Land not need to irrigate"));
        }
        return Mono.just(serverRequest)
                .flatMap(irrigationSystemService::startIrrigation)
                .flatMap(serverResponse -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(land))
                .doOnSuccess(serverResponse -> {
                    if (serverResponse.statusCode().is2xxSuccessful()) {
                        updateLandTimeSlot(land.get().getId());
                    }
                })
                .doOnError(this::alert);

    }

    private Consumer<? super ServerResponse> updateLandTimeSlot(Long landId) {
        LandConfiguration land= landConfigurationRepository.findById(landId).orElseThrow();
        if (land.getAvailableTimeSlots()!=0) {
            land.setAvailableTimeSlots(land.getAvailableTimeSlots() - 1);
        }
        land.getLand().setIrrigated(true);
        LandConfiguration updatedLand=landConfigurationRepository.save(land);

        Consumer<ServerResponse> lambdaConsumer = System.out::println;
        lambdaConsumer.accept(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(updatedLand).block());
        log.info("Consumer object", lambdaConsumer);

        return lambdaConsumer;
    }


    private Throwable alert(Throwable throwable) {
        throw  new AlertException(HttpStatus.REQUEST_TIMEOUT,"408",throwable.getLocalizedMessage());
    }



}
