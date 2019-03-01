package com.temperatures.temperatures.domain;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TemperatureEntityTest {

    @Test
    public void testConvertToFPreservesFields() {
        TemperatureEntity test = generateTemperature(0);

        TemperatureEntity result = test.convertToF();

        assertThat(result.getId(), is(test.getId()));
        assertThat(result.getCreateDate(), is(test.getCreateDate()));
        assertThat(result.getUpdateDate(), is(test.getUpdateDate()));
    }

    @Test
    public void testConvertToFConvertsTemperature() {
        TemperatureEntity test = generateTemperature(-13);

        TemperatureEntity result = test.convertToF();

        assertThat(result.getTemperatureF(), is(closeTo(8.6, 0.01)));
    }

    @Test
    public void testConvertToFDoesNotSetCelsiusTemp() {
        TemperatureEntity test = generateTemperature(20);

        TemperatureEntity result = test.convertToF();

        assertThat(result.getTemperatureC(), is(nullValue()));
    }

    private TemperatureEntity generateTemperature(double temperature) {
        TemperatureEntity entity = new TemperatureEntity();
        entity.setId(1);
        entity.setTemperatureC(temperature);
        entity.setCreateDate(new Date());
        entity.setUpdateDate(new Date());
        return entity;
    }
}
