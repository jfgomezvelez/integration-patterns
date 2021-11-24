package co.edu.eafit.model.weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class CurrentCondition {
    private final String datetime;
    private final int datetimeEpoch;
    private final double temp;
    private final double feelslike;
    private final double humidity;
    private final double dew;
    private final double precip;
    //private final Object precipprob;
    //private final Object snow;
    private final double snowdepth;
    //private final Object preciptype;
    private final double windgust;
    private final double windspeed;
    private final double winddir;
    private final double pressure;
    private final double visibility;
    private final double cloudcover;
    //private final Object solarradiation;
    //private final Object solarenergy;
    //private final Object uvindex;
    private final String conditions;
    private final String icon;
    private final List<String> stations;
    private final String sunrise;
    private final int sunriseEpoch;
    private final String sunset;
    private final int sunsetEpoch;
    private final double moonphase;
}
