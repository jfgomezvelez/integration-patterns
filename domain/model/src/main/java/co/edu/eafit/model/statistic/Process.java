package co.edu.eafit.model.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Process {

    private final String id;
    private final LocalTime initialDate;
    private final LocalTime finishDate;
    private final String name;
    private final String traceabilityIdentifier;
    private final String feature;

}
