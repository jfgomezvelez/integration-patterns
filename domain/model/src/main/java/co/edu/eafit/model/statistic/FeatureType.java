package co.edu.eafit.model.statistic;

public enum FeatureType {

    ONLYHTTP("OnlyHTTP");

    private final String type;

    FeatureType(String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }
}
