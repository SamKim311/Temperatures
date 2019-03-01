package com.temperatures.temperatures.controller;

import com.temperatures.temperatures.domain.TemperatureEntity;
import com.temperatures.temperatures.domain.TemperatureRequest;
import com.temperatures.temperatures.service.TemperaturesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/temperatures")
public class TemperaturesController {
    private static final Logger logger = LoggerFactory.getLogger(TemperaturesController.class);

    private final TemperaturesService temperaturesService;

    @Autowired
    public TemperaturesController(TemperaturesService temperaturesService) {
        this.temperaturesService = temperaturesService;
    }

    @GetMapping("/all")
    public List<TemperatureEntity> allTemperatures() {
        return temperaturesService.getAll();
    }

    @PostMapping
    public ResponseEntity<Void> addTemperature(@RequestBody TemperatureRequest tempInC) {
        if (tempInC == null) {
            logger.debug("Received null temperature during addTemperature request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        temperaturesService.createTemperature(tempInC.getTemperature());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/id/{tempId}")
    public ResponseEntity<List<TemperatureEntity>> getById(@PathVariable int tempId) {
        try {
            List<TemperatureEntity> result = temperaturesService.getByIdWithConversion(tempId);
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            logger.error("Could not find temperature entity {}", tempId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/id/{tempId}")
    public ResponseEntity<Void> updateById(@PathVariable int tempId, @RequestBody TemperatureRequest newTempC) {
        try {
            temperaturesService.updateById(tempId, newTempC.getTemperature());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Could not update temperature entity {} because it could not be found", tempId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/id/{tempId}")
    public ResponseEntity<Void> deleteById(@PathVariable int tempId) {
        try {
            temperaturesService.deleteById(tempId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Could not delete temperature entity {} because it could not be found", tempId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
