package co.edu.eafit.events.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeatherDTO {
    private int queryCost;
    private double latitude;
    private double longitude;
    private String resolvedAddress;
    private String address;
    private String timezone;
    private double tzoffset;
    private String description;
    private List<DayDTO> days;
    //private final List<Object> alerts;
    private StationsDTO stations;
    private CurrentConditionDTO currentConditions;
}
