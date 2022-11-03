package com.example.irrigationsystem.controller;

import com.example.irrigationsystem.dto.LandConfiguration;
import com.example.irrigationsystem.dto.Land;
import com.example.irrigationsystem.service.LandConfigurationService;
import com.example.irrigationsystem.service.LandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/land")
@Slf4j
public class PlotOfLandController {

    public final LandService landService;
    public final LandConfigurationService landConfigurationService;

    public PlotOfLandController(LandService landService, LandConfigurationService landConfigurationService) {
        this.landService = landService;
        this.landConfigurationService = landConfigurationService;
    }


    @GetMapping
    public List<Land> findAll() {
        return landService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Land create(@RequestBody Land landPojo) {
        return landService.add(landPojo);
    }

    @PostMapping("/configure/{landId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public LandConfiguration configure(@PathVariable("landId") Long landId, @RequestBody LandConfiguration landConfiguration) {
        return landConfigurationService.configure(landId, landConfiguration);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("id") Long id, @RequestBody Land land) {
        landService.update(id, land);
        log.info(String.valueOf(landService.update(id, land).orElseThrow()));
    }

}
