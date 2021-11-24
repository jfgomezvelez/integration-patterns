package co.edu.eafit.model.weather.gateway;

import co.edu.eafit.model.weather.Weather;
import reactor.core.publisher.Mono;

public interface WeatherRepository {
    Mono<Weather> checkWeather(String location);

    Mono<Void> requestWether(String location, String processId);
}
