package com.example.irrigationsystem.service.impl;

import com.example.irrigationsystem.dto.Land;
import com.example.irrigationsystem.repository.LandConfigurationRepository;
import com.example.irrigationsystem.repository.LandRepository;
import com.example.irrigationsystem.service.LandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LandServiceImpl implements LandService {
    private final LandRepository landRepository;
    private final LandConfigurationRepository landConfigurationRepository;


    public LandServiceImpl(LandRepository landRepository, LandConfigurationRepository landConfigurationRepository) {
        this.landRepository = landRepository;
        this.landConfigurationRepository = landConfigurationRepository;
    }

    @Override
    public List<Land> findAll() {
        return landRepository.findAll();
    }

    @Override
    public Land add(Land landPojo) {
        return landRepository.save(landPojo);
    }

    @Override
    public Optional<Land> update(long id, Land land) {
        return landRepository.findById(id)
                .map(oldItem -> {
                    Land updated = oldItem.updateWith(land,oldItem);
                    return landRepository.save(updated);
                });
    }

}
