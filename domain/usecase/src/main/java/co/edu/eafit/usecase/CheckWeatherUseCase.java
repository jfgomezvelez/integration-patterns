package co.edu.eafit.usecase;

import co.edu.eafit.model.common.Transformer;
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
public class CheckWeatherUseCase {

    private final WeatherRepository weatherRepository;
    private final StatisticRepository statisticRepository;
    private final Transformer transformer;

    public Mono<Weather> checkWeather(String location) {

        Process processInitial = Process.builder()
                .initialDate(LocalTime.now())
                .id(UUID.randomUUID().toString())
                .traceabilityIdentifier(UUID.randomUUID().toString())
                .name(ProcessType.CHECKWEATHER.toString())
                .feature(FeatureType.REQUEST_REPLAY.toString())
                .build();

        return weatherRepository.checkWeather(location)
                .flatMap(weather -> {
                    Process processFinish = processInitial
                            .toBuilder()
                            .finishDate(LocalTime.now())
                            .dataSize(transformer.toJson(weather).length())
                            .build();
                    return statisticRepository.save(processFinish).map(result -> weather);
                });
    }
}
