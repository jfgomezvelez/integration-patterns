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
public class RequestWeatherUseCase {

    private final WeatherRepository weatherRepository;
    private final StatisticRepository statisticRepository;
    private final Transformer transformer;

    public Mono<Void> requestWeather(String location) {

        Process processInitial = Process.builder()
                .initialDate(LocalTime.now())
                .id(UUID.randomUUID().toString())
                .traceabilityIdentifier(UUID.randomUUID().toString())
                .name(ProcessType.CHECKWEATHER.toString())
                .feature(FeatureType.COMMAND.toString())
                .build();


        return statisticRepository.save(processInitial).flatMap(result -> weatherRepository.requestWether(location, processInitial.getId()));
    }

    public Mono<Void> processWeather(Weather weather, String processId) {

        return statisticRepository.findById(processId)
                .flatMap(process -> {
                    Process processFinish = process
                            .toBuilder()
                            .finishDate(LocalTime.now())
                            .dataSize(transformer.toJson(weather).length())
                            .build();
                    return statisticRepository.save(processFinish).map(result -> weather);
                }).then();

    }
}
