package co.edu.eafit.model.statistic.gateway;

import co.edu.eafit.model.weather.Weather;
import reactor.core.publisher.Mono;
import co.edu.eafit.model.statistic.Process;

public interface StatisticRepository {

    Mono<Process> save(Process process);

    Mono<Process> findById(String id);
}
