package com.temperatures.temperatures.controller;

import com.temperatures.temperatures.domain.TemperatureEntity;
import com.temperatures.temperatures.domain.TemperatureRequest;
import com.temperatures.temperatures.service.TemperaturesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TemperaturesControllerTest {
    private TemperaturesController controller;

    @Mock
    private TemperaturesService mockService;

    @Before
    public void setUp() {
        controller = new TemperaturesController(mockService);
    }

    @Test
    public void testGetAllTempsCallsServiceMethod() {
        controller.allTemperatures();

        verify(mockService).getAll();
    }

    @Test
    public void testAddTemperatureWithNoTemperatureThrows() {
        ResponseEntity<Void> response = controller.addTemperature(null);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void testAddTemperatureCallsServiceMethodWithTemperature() {
        TemperatureRequest request = new TemperatureRequest();
        request.setTemperature(16.2);
        controller.addTemperature(request);

        verify(mockService).createTemperature(16.2);
    }

    @Test
    public void testGetTemperatureCallsServiceMethodWithId() {
        when(mockService.getByIdWithConversion(15)).thenReturn(Collections.emptyList());

        ResponseEntity<List<TemperatureEntity>> response = controller.getById(15);

        verify(mockService).getByIdWithConversion(15);
    }

    @Test
    public void testGetTemperatureReturns404WhenRecordDoesNotExist() {
        when(mockService.getByIdWithConversion(anyInt())).thenThrow(EntityNotFoundException.class);

        ResponseEntity<List<TemperatureEntity>> response = controller.getById(-1);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testUpdateTemperatureCallsServiceMethodWithId() {
        TemperatureRequest request = new TemperatureRequest();
        request.setTemperature(56.0);
        controller.updateById(15, request);

        verify(mockService).updateById(15, 56.0);
    }

    @Test
    public void testDeleteTemperatureCallsServiceMethodWithId() {
        controller.deleteById(15);

        verify(mockService).deleteById(15);
    }
}
