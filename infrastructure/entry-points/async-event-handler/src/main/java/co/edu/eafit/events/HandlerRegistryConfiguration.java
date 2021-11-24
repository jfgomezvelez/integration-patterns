package co.edu.eafit.events;

import co.edu.eafit.events.handlers.CommandsHandler;
import co.edu.eafit.events.handlers.QueriesHandler;
import co.edu.eafit.model.weather.Weather;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerRegistryConfiguration {

    @Bean
    public HandlerRegistry handlerRegistry(CommandsHandler commands, QueriesHandler queries) {
        return HandlerRegistry.register()
                .handleCommand("weather.response", commands::handleCommandWeatherResponse, Weather.class);
    }
}
