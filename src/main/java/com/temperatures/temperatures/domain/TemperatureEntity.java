package com.temperatures.temperatures.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "temperature")
public class TemperatureEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @JsonInclude(Include.NON_NULL)
    @Column(name = "temperature")
    private Double temperatureC;

    @JsonInclude(Include.NON_NULL)
    @Transient
    private Double temperatureF;

    @CreationTimestamp
    private Date createDate;

    @UpdateTimestamp
    private Date updateDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTemperatureC() {
        return temperatureC;
    }

    public void setTemperatureC(Double temperatureC) {
        this.temperatureC = temperatureC;
    }

    public Double getTemperatureF() {
        return temperatureF;
    }

    public void setTemperatureF(Double temperatureF) {
        this.temperatureF = temperatureF;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemperatureEntity that = (TemperatureEntity) o;
        return id.equals(that.id) &&
                Objects.equals(temperatureC, that.temperatureC) &&
                Objects.equals(temperatureF, that.temperatureF) &&
                createDate.equals(that.createDate) &&
                updateDate.equals(that.updateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, temperatureC, temperatureF, createDate, updateDate);
    }

    public TemperatureEntity convertToF() {
        final TemperatureEntity result = new TemperatureEntity();
        result.setId(id);
        result.setTemperatureF(convertToF(temperatureC));
        result.setCreateDate(createDate);
        result.setUpdateDate(updateDate);
        return result;
    }

    public static double convertToF(double tempInC) {
        return tempInC * 1.8 + 32;
    }
}
