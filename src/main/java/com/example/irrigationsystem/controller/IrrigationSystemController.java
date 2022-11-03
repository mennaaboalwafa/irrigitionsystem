package com.example.irrigationsystem.controller;

import com.example.irrigationsystem.core.constants.Constants;
import com.example.irrigationsystem.exception.InvalidInputFormatException;
import com.example.irrigationsystem.handel.IrrigationSystemHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;

@Configuration
public class IrrigationSystemController {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/irrigate/start/{landId}", beanClass = IrrigationSystemHandler.class, beanMethod = "startIrrigation",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "irrigate", tags = "irrigation-system-controller",
                            parameters = {@Parameter(name = Constants.KAFKA_KEY_REPLY_TIMEOUT_MS, in = ParameterIn.HEADER, description = "Timeout in Milliseconds", schema = @Schema(type = "integer")),
                                    @Parameter(name = "landId", in = ParameterIn.PATH, description = "landId", schema = @Schema(type = "long"))},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(type = "Object"))),
                                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                                    @ApiResponse(responseCode = "404", description = "Not Found"),
                                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
                            },
                            requestBody = @RequestBody(required = true,
                                    content = @Content(schema = @Schema()))
                    ))
    })
    RouterFunction<ServerResponse> needIrrigation(IrrigationSystemHandler irrigationSystemHandler) {

        return route()
                .POST("/irrigate/start", irrigationSystemHandler::startIrrigation)
                .onError(RuntimeException.class, (e, req) -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                .onError(InvalidInputFormatException.class, (e, req) -> badRequest().build())
                .after((req, res) -> {
                    // log.info("RESPONDED -{}", res.statusCode());
                    return res;
                })
                .build();

    }
}