package co.edu.eafit.events.handlers;

import co.edu.eafit.model.weather.Weather;
import co.edu.eafit.usecase.RequestWeatherUseCase;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableCommandListeners
public class CommandsHandler {

    private final RequestWeatherUseCase requestWeatherUseCase;

    public Mono<Void> handleCommandWeatherResponse(Command<Weather> command) {
        System.out.println("command received: " + command.getName() + " ->" + command.getData().toString());
        return requestWeatherUseCase.processWeather(command.getData());
    }
}
