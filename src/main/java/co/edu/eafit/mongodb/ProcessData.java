package co.edu.eafit.mongodb;

//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.UUID;

//@Document(collection = "process")
public class ProcessData {

    //@Id
    private String id;
    private LocalTime initialDate;
    private LocalTime finishDate;
    private String name;
    private String traceabilityIdentifier;
    private String feature;
    private int dataSize;

    public ProcessData(LocalTime initialDate,  String name, String feature) {
        this.id = UUID.randomUUID().toString();
        this.initialDate = initialDate;
        this.name = name;
        this.traceabilityIdentifier = UUID.randomUUID().toString();
        this.feature = feature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalTime getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalTime initialDate) {
        this.initialDate = initialDate;
    }

    public LocalTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalTime finishDate) {
        this.finishDate = finishDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTraceabilityIdentifier() {
        return traceabilityIdentifier;
    }

    public void setTraceabilityIdentifier(String traceabilityIdentifier) {
        this.traceabilityIdentifier = traceabilityIdentifier;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }
}
