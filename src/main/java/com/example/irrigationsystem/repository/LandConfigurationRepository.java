package com.example.irrigationsystem.repository;

import com.example.irrigationsystem.dto.LandConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandConfigurationRepository extends JpaRepository<LandConfiguration,Long> {
}
