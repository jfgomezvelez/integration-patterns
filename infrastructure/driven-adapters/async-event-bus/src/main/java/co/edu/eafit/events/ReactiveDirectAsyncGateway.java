package co.edu.eafit.events;

import co.edu.eafit.events.dto.WeatherDTO;
import co.edu.eafit.model.weather.Weather;
import co.edu.eafit.model.weather.gateway.WeatherRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.reactivecommons.async.impl.config.annotations.EnableDirectAsyncGateway;
import reactor.core.publisher.Mono;
import org.reactivecommons.utils.ObjectMapper;

import java.util.UUID;
import java.util.logging.Level;

@Log
@AllArgsConstructor
@EnableDirectAsyncGateway
public class ReactiveDirectAsyncGateway implements WeatherRepository {

    public static final String TARGET_NAME = "weather-service";

    public static final String SOME_COMMAND_NAME = "weather.request";
    public static final String SOME_QUERY_NAME = "weather.query";

    private final DirectAsyncGateway gateway;


    @Override
    public Mono<Weather> checkWeather(String location) {
        log.log(Level.INFO, "Sending query request: {0}: {1}", new String[]{SOME_QUERY_NAME, location});
        WeatherQuery weatherQuery = new WeatherQuery();
        weatherQuery.setLocation(location);
        AsyncQuery query = new AsyncQuery<>(SOME_QUERY_NAME, weatherQuery);

        return gateway
                .requestReply(query, TARGET_NAME, WeatherDTO.class)
                .map(weatherDTO -> Converter.toDomain((WeatherDTO) weatherDTO));
    }

    @Override
    public Mono<Void> requestWether(String location, String processId) {
        log.log(Level.INFO, "Sending command: {0}: {1}", new String[]{SOME_COMMAND_NAME, location});
        WeatherCommand weatherCommand = new WeatherCommand();
        weatherCommand.setLocation(location);
        weatherCommand.setProcessId(processId);
        return gateway.sendCommand(new Command<>(SOME_COMMAND_NAME, UUID.randomUUID().toString(), weatherCommand),
                TARGET_NAME);
    }
}
