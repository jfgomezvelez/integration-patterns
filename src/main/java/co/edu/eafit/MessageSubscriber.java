package co.edu.eafit;

import lombok.extern.java.Log;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.context.annotation.Configuration;

@Log
@Configuration
public class MessageSubscriber {

    private MessagePublisher messagePublisher;

    public MessageSubscriber(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(
            value = "weather.request.queue",
            arguments = {
                    @Argument(
                            name = "x-max-length-bytes",
                            value = "30000000",
                            type = "java.lang.Integer"
                    ),
                    @Argument(
                            name = "x-queue-type",
                            value = "classic",
                            type = "java.lang.String"
                    )
            }
    ),
            exchange = @Exchange(value = "weather.request.exchange", type = ExchangeTypes.TOPIC),
            key = "weather.request")
    )
    public void solicituClimaGenerado(String mensaje) {
        log.info("Recibiendo solicitud ".concat(mensaje));
        messagePublisher.solicituClimaGenerado("{\n" +
                "    \"queryCost\": 1,\n" +
                "    \"latitude\": 6.24589,\n" +
                "    \"longitude\": -75.5746,\n" +
                "    \"resolvedAddress\": \"Medellín, Colombia\",\n" +
                "    \"address\": \"Medellin,ANT,CO\",\n" +
                "    \"timezone\": \"America/Bogota\",\n" +
                "    \"tzoffset\": -5.0,\n" +
                "    \"description\": \"Similar temperatures continuing with a chance of rain multiple days.\"\n" +
                "}");
    }
}
