package co.edu.eafit.statistic;

public enum FeatureType {

    BASICONLYHTTP("BasicOnlyHTTP");

    private final String type;

    FeatureType(String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }
}
