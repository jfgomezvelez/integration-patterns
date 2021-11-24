package co.edu.eafit.model.common;

public interface Transformer {
    <T> String toJson(T value);
}
