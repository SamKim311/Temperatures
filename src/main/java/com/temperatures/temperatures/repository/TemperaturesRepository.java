package com.temperatures.temperatures.repository;

import com.temperatures.temperatures.domain.TemperatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperaturesRepository extends JpaRepository<TemperatureEntity, Integer> {
}
