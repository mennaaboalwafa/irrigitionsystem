package com.example.irrigationsystem.handle;

import com.example.irrigationsystem.core.constants.Constants;
import com.example.irrigationsystem.exception.AlertException;
import com.example.irrigationsystem.exception.GeneralException;
import com.example.irrigationsystem.handel.IrrigationSystemHandler;
import com.example.irrigationsystem.dto.Land;
import com.example.irrigationsystem.dto.LandConfiguration;
import com.example.irrigationsystem.model.SensorDevice;
import com.example.irrigationsystem.core.constants.SoilType;
import com.example.irrigationsystem.repository.LandConfigurationRepository;
import com.example.irrigationsystem.repository.LandRepository;
import com.example.irrigationsystem.service.IrrigationSystemService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IrrigationSystemHandlerTest {
    @Mock
    IrrigationSystemService irrigationSystemService;

    @Mock
    LandRepository landRepository;

    @Mock
    LandConfigurationRepository landConfigurationRepository;

    @InjectMocks
    IrrigationSystemHandler irrigationSystemHandler;

    @Test
    void startIrrigation_withValidInputs() {
        Land land = Land.builder().id(1L).landArea(10L).landLocation("location").agriculturalCrop("bisila").irrigated(false).build();
        LandConfiguration landConfiguration = LandConfiguration.builder().id(1L).temperature(30D).waterNeeded(50L).soilType(SoilType.LOAMY).availableTimeSlots(5L).land(land).build();
        LandConfiguration updatedLandConfiguration = LandConfiguration.builder().id(1L).temperature(30D).waterNeeded(50L).soilType(SoilType.LOAMY).availableTimeSlots(4L).land(land).build();


        MockServerRequest serverRequest = MockServerRequest.builder().header(Constants.KAFKA_KEY_REPLY_TIMEOUT_MS, "30").method(HttpMethod.POST)
                .pathVariable("landId", String.valueOf(1L)).build();

        doReturn(Optional.of(land)).when(landRepository).findById(1L);
        doReturn(Optional.of(landConfiguration)).when(landConfigurationRepository).findById(1L);

        doAnswer(ans -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(
                SensorDevice.builder().id(1L).correlationId("4586087413hsj").isAvailable(true).build()
        )).when(irrigationSystemService).startIrrigation(serverRequest);

        doReturn(updatedLandConfiguration).when(landConfigurationRepository).save(landConfiguration);


        StepVerifier.create(irrigationSystemHandler.startIrrigation(serverRequest))
                .consumeNextWith(serverResponse -> assertThat(serverResponse.statusCode(), Matchers.equalTo(HttpStatus.OK)))
                .verifyComplete();
    }

    @Test
    void startIrrigation_withLand_alreadyIrrigated() {
        Land land = Land.builder().id(1L).landArea(10L).landLocation("location").agriculturalCrop("bisila").irrigated(true).build();


        MockServerRequest serverRequest = MockServerRequest.builder().header(Constants.KAFKA_KEY_REPLY_TIMEOUT_MS, "30").method(HttpMethod.POST)
                .pathVariable("landId", String.valueOf(1L)).build();

        doReturn(Optional.of(land)).when(landRepository).findById(1L);

        StepVerifier.create(irrigationSystemHandler.startIrrigation(serverRequest))
                .expectError(GeneralException.class)
                .verify();
    }


    @Test
    void startIrrigation_withLand_SensorNotAvailable() {
        Land land = Land.builder().id(1L).landArea(10L).landLocation("location").agriculturalCrop("bisila").build();

        MockServerRequest serverRequest = MockServerRequest.builder().header(Constants.KAFKA_KEY_REPLY_TIMEOUT_MS, "30").method(HttpMethod.POST)
                .pathVariable("landId", String.valueOf(1L)).build();


        doReturn(Optional.of(land)).when(landRepository).findById(1L);
        doThrow(new AlertException()).when(irrigationSystemService).startIrrigation(serverRequest);


        StepVerifier.create(irrigationSystemHandler.startIrrigation(serverRequest))
                .expectError(AlertException.class)
                .verify();
    }
}
