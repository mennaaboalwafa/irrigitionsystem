package com.example.irrigationsystem.service.impl;

import com.example.irrigationsystem.dto.Land;
import com.example.irrigationsystem.dto.LandConfiguration;
import com.example.irrigationsystem.repository.LandConfigurationRepository;
import com.example.irrigationsystem.repository.LandRepository;
import com.example.irrigationsystem.service.LandConfigurationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LandConfigurationServiceImpl implements LandConfigurationService {
    private final LandRepository landRepository;
    private final LandConfigurationRepository landConfigurationRepository;

    public LandConfigurationServiceImpl(LandRepository landRepository, LandConfigurationRepository landConfigurationRepository) {
        this.landRepository = landRepository;
        this.landConfigurationRepository = landConfigurationRepository;
    }

    @Override
    public LandConfiguration configure(Long landId, LandConfiguration landConfiguration) {
        Optional<Land> land = landRepository.findById(landId);
        if (land.isPresent()) {
            landConfiguration.setLand(land.get());
            return landConfigurationRepository.save(landConfiguration);
        }
        return null;
    }
}
