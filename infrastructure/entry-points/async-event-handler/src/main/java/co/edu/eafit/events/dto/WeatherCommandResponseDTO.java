package co.edu.eafit.events.dto;

import lombok.Data;

@Data
public class WeatherCommandResponseDTO {
    private WeatherDTO weather;
    private String processId;
}
