package co.edu.eafit.api;

import co.edu.eafit.usecase.CheckWeatherUseCase;
import co.edu.eafit.usecase.RequestWeatherUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class Handler {

    private final RequestWeatherUseCase requestWeatherUseCase;
    private  final CheckWeatherUseCase checkWeatherUseCase;

    public Mono<ServerResponse> checkWeather(ServerRequest serverRequest) {

        return checkWeatherUseCase.checkWeather(serverRequest.pathVariable("location"))
                .flatMap(weather ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(weather))
                )
                .onErrorResume(error -> ServerResponse.badRequest().body(fromValue(error.getMessage())));
    }

    public Mono<ServerResponse> requestWeather(ServerRequest serverRequest) {

        return requestWeatherUseCase.requestWeather(serverRequest.pathVariable("location"))
                .flatMap(weather ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(weather))
                )
                .onErrorResume(error -> ServerResponse.badRequest().body(fromValue(error.getMessage())));
    }
}
