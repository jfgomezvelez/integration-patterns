package co.edu.eafit.model.statistic.gateway;

import reactor.core.publisher.Mono;
import co.edu.eafit.model.statistic.Process;

public interface StatisticRepository {

    Mono<Process> save(Process process);
}
