package co.edu.eafit.events;

import co.edu.eafit.events.dto.WeatherCommandResponseDTO;
import co.edu.eafit.events.dto.WeatherDTO;
import co.edu.eafit.events.handlers.CommandsHandler;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerRegistryConfiguration {

    @Bean
    public HandlerRegistry handlerRegistry(CommandsHandler commands) {
        return HandlerRegistry.register()
                .handleCommand("weather.response", commands::handleCommandWeatherResponse, WeatherCommandResponseDTO.class);
    }
}
