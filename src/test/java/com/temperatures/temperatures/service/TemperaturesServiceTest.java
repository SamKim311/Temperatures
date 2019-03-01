package com.temperatures.temperatures.service;

import com.temperatures.temperatures.domain.TemperatureEntity;
import com.temperatures.temperatures.repository.TemperaturesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TemperaturesServiceTest {
    private TemperaturesService temperaturesService;

    @Mock
    private TemperaturesRepository mockRepository;

    @Before
    public void setUp() {
        temperaturesService = new TemperaturesService(mockRepository);

        when(mockRepository.findAll()).thenReturn(Collections.emptyList());
    }

    @Test
    public void testThatGetAllTempReturnsEmptyListWhenRepositoryReturnsNoResults() {
        List<TemperatureEntity> result = temperaturesService.getAll();

        assertThat(result, is(not(nullValue())));
        assertThat(result.size(), is(0));
    }

    @Test
    public void testThatGetAllTempDuplicatesRecordInFahrenheitWhenGivenOneRecord() {
        when(mockRepository.findAll()).thenReturn(Collections.singletonList(generateTemperature(1, 0)));

        List<TemperatureEntity> result = temperaturesService.getAll();

        assertThat(result.size(), is(2));

        TemperatureEntity dupeRecord = result.get(1);

        assertThat(dupeRecord.getTemperatureF(), is(closeTo(32, 0.01)));
    }

    @Test
    public void testThatGetAllTempMaintainsOrderOfRecords() {
        when(mockRepository.findAll()).thenReturn(Arrays.asList(generateTemperature(1, 0), generateTemperature(2, 100)));

        List<TemperatureEntity> result = temperaturesService.getAll();

        assertThat(result.size(), is(4));

        assertThat(result.get(0).getId(), is(1));
        assertThat(result.get(1).getId(), is(1));
        assertThat(result.get(2).getId(), is(2));
        assertThat(result.get(3).getId(), is(2));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetByIdThrowsWhenRecordIsNotFound() {
        when(mockRepository.findOne(anyInt())).thenReturn(null);

        temperaturesService.getByIdWithConversion(-1);
    }

    @Test
    public void testGetByIdCreatesDuplicateRecordInF() {
        when(mockRepository.findOne(anyInt())).thenReturn(generateTemperature(3, 24));

        List<TemperatureEntity> result = temperaturesService.getByIdWithConversion(3);

        assertThat(result.size(), is(2));
        assertThat(result.get(1).getTemperatureF(), is(closeTo(75.2, 0.01)));
    }

    @Test
    public void testCreateTemperaturePersistsThroughRepository() {
        temperaturesService.createTemperature(14.4);

        // Admittedly not incredibly helpful - if the creation was somewhat complicated I would capture the argument
        // and ensure the fields are set correctly, but I feel the effort isn't worth it for this simplicity
        verify(mockRepository).save(any(TemperatureEntity.class));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateByIdThrowsWhenRecordIsNotFound() {
        when(mockRepository.findOne(anyInt())).thenReturn(null);

        temperaturesService.updateById(-1, 2);
    }

    @Test
    public void testUpdateByIdCallsRepositoryMethod() {
        TemperatureEntity temp = generateTemperature(4, 20);
        when(mockRepository.findOne(4)).thenReturn(temp);

        temperaturesService.updateById(4, 22);

        verify(mockRepository).save(temp);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByIdThrowsWhenRecordIsNotFound() {
        TemperatureEntity temp = generateTemperature(5, 20);
        when(mockRepository.exists(5)).thenReturn(false);

        temperaturesService.deleteById(5);

        verify(mockRepository).delete(5);
    }

    @Test
    public void testDeleteByIdCallsRepositoryMethod() {
        when(mockRepository.exists(5)).thenReturn(true);

        temperaturesService.deleteById(5);

        verify(mockRepository).delete(5);
    }

    private TemperatureEntity generateTemperature(int id, double tempInC) {
        TemperatureEntity entity = new TemperatureEntity();
        entity.setId(id);
        entity.setTemperatureC(tempInC);
        entity.setCreateDate(new Date());
        entity.setUpdateDate(new Date());
        return entity;
    }
}
