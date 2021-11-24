package co.edu.eafit.model.statistic;

public enum ProcessType {

    CHECKWEATHER("CheckWeather");

    private final String type;

    ProcessType(String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }
}
