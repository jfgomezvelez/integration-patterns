package co.edu.eafit;

import com.rabbitmq.client.Channel;
import lombok.extern.java.Log;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Log
@Component
public class MessagePattern {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(
            value = "weather.request.queue"
    ),
            exchange = @Exchange(value = "weather.exchange", type = ExchangeTypes.TOPIC),
            key = "weather.request")
    )
    public void receive(Message message, Channel channel) {
        log.info("Recibiendo mensaje ".concat(new String(message.getBody())).concat(" messageId ").concat(message.getMessageProperties().getMessageId()));
        boolean result = send("{\n" +
                "    \"queryCost\": 1,\n" +
                "    \"latitude\": 6.24589,\n" +
                "    \"longitude\": -75.5746,\n" +
                "    \"resolvedAddress\": \"Medellín, Colombia\",\n" +
                "    \"address\": \"Medellin,ANT,CO\",\n" +
                "    \"timezone\": \"America/Bogota\",\n" +
                "    \"tzoffset\": -5.0,\n" +
                "    \"description\": \"Similar temperatures continuing with a chance of rain multiple days.\"\n" +
                "}", message.getMessageProperties().getMessageId());
        try {
            if (result) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean send(String message, String messageId) {
        log.info("Enviando evento "
                .concat(message)
                .concat(" a ").concat("weather.response.exchange")
                .concat(":")
                .concat("weather.response"));

        byte[] data = message.getBytes(StandardCharsets.UTF_8);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setMessageId(messageId);

        Message messageToSend = new Message(data, messageProperties);
        rabbitTemplate.convertAndSend("weather.exchange", "weather.response", messageToSend);
        return true;
    }
}
