package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class SensorMessage implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    private int idDevice;
    private String generationDate;
    private double temperature;
    private double humidity;

    public SensorMessage() {
    }

    public SensorMessage(int idDevice, String generationDate, double temperature, double humidity) {
        this.idDevice = idDevice;
        this.generationDate = generationDate;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(int idDevice) {
        this.idDevice = idDevice;
    }

    public String getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(String generationDate) {
        this.generationDate = generationDate;
    }
}