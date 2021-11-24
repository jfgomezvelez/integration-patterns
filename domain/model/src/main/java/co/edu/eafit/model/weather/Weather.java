package co.edu.eafit.model.weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Weather {
    private final int queryCost;
    private final double latitude;
    private final double longitude;
    private final String resolvedAddress;
    private final String address;
    private final String timezone;
    private final double tzoffset;
    private final String description;
    private final List<Day> days;
    //private final List<Object> alerts;
    private final Stations stations;
    private final CurrentCondition currentConditions;
}
