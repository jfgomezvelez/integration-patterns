package co.edu.eafit;

import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Log
@Component
public class MessagePublisher {

    private RabbitTemplate rabbitTemplate;

    public MessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void solicituClimaGenerado(String message) {
        log.info("Enviando evento "
                .concat(message)
                .concat(" a ").concat("weather.response.exchange")
                .concat(":")
                .concat("weather.response"));
        rabbitTemplate.convertAndSend("weather.response.exchange", "weather.response", message);
    }
}
