package com.example.irrigationsystem.controller;


import com.example.irrigationsystem.dto.Land;
import com.example.irrigationsystem.dto.LandConfiguration;
import com.example.irrigationsystem.core.constants.SoilType;
import com.example.irrigationsystem.repository.LandConfigurationRepository;
import com.example.irrigationsystem.repository.LandRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class PlotOfLandControllerIT {

    @Autowired
    private LandRepository landRepository;
    @Autowired
    private LandConfigurationRepository landConfigurationRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    private List<Land> landList;


    @BeforeEach
    void setup() {
        landRepository.deleteAll();
        landConfigurationRepository.deleteAll();
        landList = List.of(new Land(10L, "ismilia", "tamatm"),
                new Land(200L, "ismilia", "felfel"));

        landList=landRepository.saveAll(landList);

        List<LandConfiguration> landConfigurations = List.of(
                LandConfiguration.builder().id(1L).soilType(SoilType.LOAMY).waterNeeded(50L).temperature(43D).availableTimeSlots(5L).land(landList.get(0)).build(),
                LandConfiguration.builder().id(2L).soilType(SoilType.CLAY).waterNeeded(70L).temperature(50D).availableTimeSlots(6L).land(landList.get(1)).build()

        );

        landConfigurationRepository.saveAll(landConfigurations);
    }

    @Test
    public void given_whenFindAll_thenFindAllLand() throws Exception {

        ResultActions response = mockMvc.perform(get("/land"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(landList.size())));
    }


    @Test
    public void givenLandObject_whenCreateLand_thenReturnSavedLand() throws Exception {

        landRepository.deleteAll(landList);
        // given - precondition or setup
        Land land = new Land(500L, "rice", "alex Road");

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/land")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(land)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.landLocation",
                        is(land.getLandLocation())))
                .andExpect(jsonPath("$.landArea",
                        is(land.getLandArea().intValue())))
                .andExpect(jsonPath("$.agriculturalCrop",
                        is(land.getAgriculturalCrop())));

    }

    // JUnit test for update land REST API - positive scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        // given - precondition or setup
        Land land = landList.get(0);
        land.setLandArea(100L);
        landRepository.save(land);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/land/{id}", land.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(land)));


        // then - verify the output
        response.andExpect(status().isOk());
    }


    @Test
    public void givenLandConfigurationObject_whenCreateConfiguration_thenReturnSavedLand() throws Exception {

        landConfigurationRepository.deleteAll();
        // given - precondition or setup
        LandConfiguration landConfiguration = LandConfiguration.builder().soilType(SoilType.LOAMY).waterNeeded(50L).temperature(43D).availableTimeSlots(5L).build();

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/land/configure/{landId}", landList.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(landConfiguration)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.soilType",
                        is(landConfiguration.getSoilType().toString())))
                .andExpect(jsonPath("$.waterNeeded",
                        is(landConfiguration.getWaterNeeded().intValue())))
                .andExpect(jsonPath("$.availableTimeSlots",
                        is(landConfiguration.getAvailableTimeSlots().intValue())))
                .andExpect(jsonPath("$.temperature",
                        is(landConfiguration.getTemperature())));
    }
}
