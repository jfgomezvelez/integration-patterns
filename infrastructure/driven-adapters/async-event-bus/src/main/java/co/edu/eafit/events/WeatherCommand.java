package co.edu.eafit.events;

import lombok.Data;

@Data
public class WeatherCommand {
    private String location;
    private String processId;
}
