package co.edu.eafit.events;

import co.edu.eafit.events.handlers.CommandsHandler;
import co.edu.eafit.events.handlers.EventsHandler;
import co.edu.eafit.events.handlers.QueriesHandler;
import co.edu.eafit.model.weather.Weather;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerRegistryConfiguration {

    @Bean
    public HandlerRegistry handlerRegistry(CommandsHandler commands, EventsHandler events, QueriesHandler queries) {
        return HandlerRegistry.register()
                .listenNotificationEvent("some.broadcast.event.name", events::handleEventA, Object.class/*change for proper model*/)
                .listenEvent("some.event.name", events::handleEventA, Object.class/*change for proper model*/)
                .handleCommand("weather.response", commands::handleCommandWeatherResponse, Weather.class/*change for proper model*/)
                .serveQuery("some.query.name", queries::handleQueryA, Object.class/*change for proper model*/);
    }
}
