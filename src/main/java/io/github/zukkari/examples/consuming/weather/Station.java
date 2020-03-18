package io.github.zukkari.examples.consuming.weather;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Station {
    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "airpressure")
    private Double airPressure;

    @JacksonXmlProperty(localName = "relativehumidity")
    private Double relativeHumidity;

    @JacksonXmlProperty(localName = "airtemperature")
    private Double temperature;

    @JacksonXmlProperty(localName = "winddirection")
    private Double windDirection;

    @JacksonXmlProperty(localName = "windspeed")
    private Double windSpeed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(Double airPressure) {
        this.airPressure = airPressure;
    }

    public Double getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(Double relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(Double windDirection) {
        this.windDirection = windDirection;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", airPressure=" + airPressure +
                ", relativeHumidity=" + relativeHumidity +
                ", temperature=" + temperature +
                ", windDirection=" + windDirection +
                ", windSpeed=" + windSpeed +
                '}';
    }
}
