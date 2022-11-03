package com.example.irrigationsystem.repository;

import com.example.irrigationsystem.dto.Land;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandRepository extends JpaRepository<Land,Long> {

    @Override
    List<Land> findAll();

    List<Land> findAllByIrrigatedIsTrue ();
}
