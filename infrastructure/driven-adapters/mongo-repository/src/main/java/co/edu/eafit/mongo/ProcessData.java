package co.edu.eafit.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@Data
@Document(collection = "process")
@NoArgsConstructor
public class ProcessData {

    @Id
    private String id;
    private LocalTime initialDate;
    private LocalTime finishDate;
    private String name;
    private String traceabilityIdentifier;
    private String feature;
}
