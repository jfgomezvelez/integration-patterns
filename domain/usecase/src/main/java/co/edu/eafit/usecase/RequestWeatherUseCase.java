package co.edu.eafit.usecase;

import co.edu.eafit.model.statistic.FeatureType;
import co.edu.eafit.model.statistic.Process;
import co.edu.eafit.model.statistic.ProcessType;
import co.edu.eafit.model.statistic.gateway.StatisticRepository;
import co.edu.eafit.model.weather.Weather;
import co.edu.eafit.model.weather.gateway.WeatherRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.UUID;

@RequiredArgsConstructor
public class RequestWeatherUseCase {

    private final WeatherRepository weatherRepository;
    private final StatisticRepository statisticRepository;

    public Mono<Void> requestWeather(String location) {

        Process processInitial = Process.builder()
                .initialDate(LocalTime.now())
                .id(UUID.randomUUID().toString())
                .traceabilityIdentifier(UUID.randomUUID().toString())
                .name(ProcessType.CHECKWEATHER.toString())
                .feature(FeatureType.ONLYHTTP.toString())
                .build();

        return weatherRepository.requestWether(location)
                .flatMap(weather -> {
                    Process processFinish = processInitial
                            .toBuilder()
                            .finishDate(LocalTime.now())
                            .build();
                    return statisticRepository.save(processFinish).map(result -> weather);
                });
    }

    public Mono<Void> processWeather(Weather weather){
        return Mono.empty();
    }
}
