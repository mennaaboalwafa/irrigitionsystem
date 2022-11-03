package com.example.irrigationsystem.service;

import com.example.irrigationsystem.dto.Land;

import java.util.List;
import java.util.Optional;


public interface LandService {
    List<Land> findAll ();
    Land add(Land landPojo);
    Optional<Land> update(long id, Land land);
}
