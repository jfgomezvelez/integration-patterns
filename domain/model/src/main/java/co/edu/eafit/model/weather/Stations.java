package co.edu.eafit.model.weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Stations {
    private final SKMD SKMD;
    private final F0688 F0688;
    private final SKRG SKRG;
}
