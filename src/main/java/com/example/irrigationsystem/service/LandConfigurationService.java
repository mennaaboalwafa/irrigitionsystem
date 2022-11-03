package com.example.irrigationsystem.service;

import com.example.irrigationsystem.dto.LandConfiguration;

public interface LandConfigurationService {

    LandConfiguration configure(Long landId, LandConfiguration landConfiguration);
}
