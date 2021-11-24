package co.edu.eafit.model.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public abstract class StationBase {
    private double distance;
    private double latitude;
    private double longitude;
    private int useCount;
    private String id;
    private String name;
    private int quality;
    private double contribution;
}
