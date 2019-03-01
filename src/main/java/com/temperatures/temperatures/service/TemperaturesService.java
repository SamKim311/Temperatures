package com.temperatures.temperatures.service;

import com.temperatures.temperatures.domain.TemperatureEntity;
import com.temperatures.temperatures.repository.TemperaturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TemperaturesService {
    private final TemperaturesRepository temperaturesRepository;

    @Autowired
    public TemperaturesService(TemperaturesRepository temperaturesRepository) {
        this.temperaturesRepository = temperaturesRepository;
    }

    public List<TemperatureEntity> getAll() {
        List<TemperatureEntity> result = new ArrayList<>();

        List<TemperatureEntity> records = temperaturesRepository.findAll();
        for (TemperatureEntity record : records) {
            result.add(record);
            result.add(record.convertToF());
        }

        return result;
    }

    public List<TemperatureEntity> getByIdWithConversion(int id) {
        TemperatureEntity entity = getById(id);

        TemperatureEntity dupe = entity.convertToF();

        return Arrays.asList(entity, dupe);
    }

    private TemperatureEntity getById(int id) {
        TemperatureEntity entity = temperaturesRepository.findOne(id);
        if (entity == null) {
            throw new EntityNotFoundException("Could not find record with id " + id);
        }
        return entity;
    }

    public void createTemperature(double tempInC) {
        TemperatureEntity newTemp = new TemperatureEntity();
        newTemp.setTemperatureC(tempInC);
        temperaturesRepository.save(newTemp);
    }

    public void updateById(int id, double newTempC) {
        TemperatureEntity entity = getById(id);

        entity.setTemperatureC(newTempC);

        temperaturesRepository.save(entity);
    }

    public void deleteById(int id) {
        if (!temperaturesRepository.exists(id)) {
            throw new EntityNotFoundException("Could not find record with id " + id);
        }
        temperaturesRepository.delete(id);
    }
}
