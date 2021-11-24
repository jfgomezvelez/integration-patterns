package co.edu.eafit.events.dto;

import lombok.Data;

@Data
public abstract class StationBaseDTO {
    private double distance;
    private double latitude;
    private double longitude;
    private int useCount;
    private String id;
    private String name;
    private int quality;
    private double contribution;
}
