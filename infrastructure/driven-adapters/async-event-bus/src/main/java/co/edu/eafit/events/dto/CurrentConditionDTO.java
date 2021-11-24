package co.edu.eafit.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CurrentConditionDTO {
    private  String datetime;
    private  int datetimeEpoch;
    private  double temp;
    private  double feelslike;
    private  double humidity;
    private  double dew;
    private  double precip;
    //private  Object precipprob;
    //private  Object snow;
    private  double snowdepth;
    //private  Object preciptype;
    private  double windgust;
    private  double windspeed;
    private  double winddir;
    private  double pressure;
    private  double visibility;
    private  double cloudcover;
    //private  Object solarradiation;
    //private  Object solarenergy;
    //private  Object uvindex;
    private  String conditions;
    private  String icon;
    private  List<String> stations;
    private  String sunrise;
    private  int sunriseEpoch;
    private  String sunset;
    private  int sunsetEpoch;
    private  double moonphase;
}
