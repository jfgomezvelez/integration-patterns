package co.edu.eafit;

import com.google.gson.Gson;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Log
@Component
public class MessagePattern {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.listener.simple.acknowledge-mode}")
    private String mode;

    @RabbitListener(queues = "weather.api.cm.queue", concurrency = "${process.concurrency}")
    public void receive(Message message, Channel channel) {

        log.info("Recibiendo mensaje ".concat(new String(message.getBody())).concat(" messageId ").concat(message.getMessageProperties().getMessageId()));

        co.edu.eafit.Message messageReceive = new Gson().fromJson(new String(message.getBody()), co.edu.eafit.Message.class);

        String messageToSend = "Empty";

        switch (messageReceive.getMessageType())
        {
            case 1 : messageToSend = createSmallMessage();
            break;
            case 2 : messageToSend = createMediumMessage();
            break;
            case 3 : messageToSend = createBigMessage();
            break;
        }

        boolean result = send(messageToSend, message.getMessageProperties().getMessageId());

        if("manual".equals(mode)){
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
    }

    public boolean send(String message, String messageId) {
        log.info("Enviando evento "
                .concat(message)
                .concat(" a ").concat("weather.service.cm.exchange ")
                .concat(": ")
                .concat("weather.service.cm.queue"));

        byte[] data = message.getBytes(StandardCharsets.UTF_8);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setMessageId(messageId);

        Message messageToSend = new Message(data, messageProperties);
        rabbitTemplate.convertAndSend("weather.service.cm.exchange","weather.service.cm.routingkey",  messageToSend);
        return true;
    }

    private String createSmallMessage(){
        return "{\n" +
                "    \"queryCost\": 1,\n" +
                "    \"latitude\": 6.24589,\n" +
                "    \"longitude\": -75.5746,\n" +
                "    \"resolvedAddress\": \"Medellín, Colombia\",\n" +
                "    \"address\": \"Medellin,ANT,CO\",\n" +
                "    \"timezone\": \"America/Bogota\",\n" +
                "    \"tzoffset\": -5.0,\n" +
                "    \"description\": \"Similar temperatures continuing with a chance of rain multiple days.\"\n"+
                "}";
    }

    private String createMediumMessage(){
        return "{\n" +
                "    \"queryCost\": 1,\n" +
                "    \"latitude\": 6.24589,\n" +
                "    \"longitude\": -75.5746,\n" +
                "    \"resolvedAddress\": \"Medellín, Colombia\",\n" +
                "    \"address\": \"Medellín,ANT,CO\",\n" +
                "    \"timezone\": \"America/Bogota\",\n" +
                "    \"tzoffset\": -5.0,\n" +
                "    \"description\": \"Similar temperatures continuing with a chance of rain multiple days.\",\n" +
                "    \"days\": [\n" +
                "        {\n" +
                "            \"datetime\": \"2022-02-09\",\n" +
                "            \"datetimeEpoch\": 1644382800,\n" +
                "            \"tempmax\": 73.1,\n" +
                "            \"tempmin\": 55.3,\n" +
                "            \"temp\": 63.7,\n" +
                "            \"feelslikemax\": 73.1,\n" +
                "            \"feelslikemin\": 55.3,\n" +
                "            \"feelslike\": 63.7,\n" +
                "            \"dew\": 60.2,\n" +
                "            \"humidity\": 89.6,\n" +
                "            \"precip\": 0.32,\n" +
                "            \"precipprob\": 100.0,\n" +
                "            \"precipcover\": null,\n" +
                "            \"preciptype\": [\n" +
                "                \"rain\"\n" +
                "            ],\n" +
                "            \"snow\": null,\n" +
                "            \"snowdepth\": 0.0,\n" +
                "            \"windgust\": 8.7,\n" +
                "            \"windspeed\": 17.9,\n" +
                "            \"winddir\": 215.8,\n" +
                "            \"pressure\": 1018.2,\n" +
                "            \"cloudcover\": 88.6,\n" +
                "            \"visibility\": 7.1,\n" +
                "            \"solarradiation\": 210.8,\n" +
                "            \"solarenergy\": 16.6,\n" +
                "            \"uvindex\": 3.0,\n" +
                "            \"severerisk\": 10.0,\n" +
                "            \"sunrise\": \"06:19:36\",\n" +
                "            \"sunriseEpoch\": 1644405576,\n" +
                "            \"sunset\": \"18:13:33\",\n" +
                "            \"sunsetEpoch\": 1644448413,\n" +
                "            \"moonphase\": 0.29,\n" +
                "            \"conditions\": \"Rain, Partially cloudy\",\n" +
                "            \"description\": \"Partly cloudy throughout the day with rain.\",\n" +
                "            \"icon\": \"rain\",\n" +
                "            \"stations\": [\n" +
                "                \"SKMD\",\n" +
                "                \"SKRG\"\n" +
                "            ],\n" +
                "            \"source\": \"comb\",\n" +
                "            \"hours\": [\n" +
                "                {\n" +
                "                    \"datetime\": \"00:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644382800,\n" +
                "                    \"temp\": 57.1,\n" +
                "                    \"feelslike\": 57.1,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 57.1,\n" +
                "                    \"precip\": null,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 1.6,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1026.0,\n" +
                "                    \"visibility\": 4.3,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-night\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"01:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644386400,\n" +
                "                    \"temp\": 57.1,\n" +
                "                    \"feelslike\": 57.1,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 57.1,\n" +
                "                    \"precip\": null,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 1.8,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1026.0,\n" +
                "                    \"visibility\": 3.7,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": null,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-night\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"02:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644390000,\n" +
                "                    \"temp\": 57.1,\n" +
                "                    \"feelslike\": 57.1,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 57.1,\n" +
                "                    \"precip\": null,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 1.8,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1024.0,\n" +
                "                    \"visibility\": 3.7,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-night\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"03:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644393600,\n" +
                "                    \"temp\": 57.1,\n" +
                "                    \"feelslike\": 57.1,\n" +
                "                    \"humidity\": 93.7,\n" +
                "                    \"dew\": 55.3,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.8,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1024.0,\n" +
                "                    \"visibility\": 5.0,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-night\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"04:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644397200,\n" +
                "                    \"temp\": 55.3,\n" +
                "                    \"feelslike\": 55.3,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 55.3,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.8,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1023.0,\n" +
                "                    \"visibility\": 3.7,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-night\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"05:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644400800,\n" +
                "                    \"temp\": 55.3,\n" +
                "                    \"feelslike\": 55.3,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 55.3,\n" +
                "                    \"precip\": 0.01,\n" +
                "                    \"precipprob\": 100.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 2.0,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1024.0,\n" +
                "                    \"visibility\": 3.7,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Partially cloudy\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"06:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644404400,\n" +
                "                    \"temp\": 64.0,\n" +
                "                    \"feelslike\": 64.0,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 64.0,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 2.0,\n" +
                "                    \"windspeed\": 4.6,\n" +
                "                    \"winddir\": 320.0,\n" +
                "                    \"pressure\": 1017.2,\n" +
                "                    \"visibility\": 4.3,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-night\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"07:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644408000,\n" +
                "                    \"temp\": 64.0,\n" +
                "                    \"feelslike\": 64.0,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 64.0,\n" +
                "                    \"precip\": null,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 2.0,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1020.2,\n" +
                "                    \"visibility\": 4.8,\n" +
                "                    \"cloudcover\": 88.4,\n" +
                "                    \"solarradiation\": null,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-day\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"08:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644411600,\n" +
                "                    \"temp\": 67.6,\n" +
                "                    \"feelslike\": 67.6,\n" +
                "                    \"humidity\": 83.21,\n" +
                "                    \"dew\": 62.3,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.6,\n" +
                "                    \"windspeed\": 0.1,\n" +
                "                    \"winddir\": 0.0,\n" +
                "                    \"pressure\": 1020.2,\n" +
                "                    \"visibility\": 4.3,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 208.0,\n" +
                "                    \"solarenergy\": 0.7,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-day\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"09:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644415200,\n" +
                "                    \"temp\": 69.5,\n" +
                "                    \"feelslike\": 69.5,\n" +
                "                    \"humidity\": 77.89,\n" +
                "                    \"dew\": 62.3,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.6,\n" +
                "                    \"windspeed\": 4.7,\n" +
                "                    \"winddir\": 22.0,\n" +
                "                    \"pressure\": 1021.2,\n" +
                "                    \"visibility\": 4.4,\n" +
                "                    \"cloudcover\": 86.7,\n" +
                "                    \"solarradiation\": 316.0,\n" +
                "                    \"solarenergy\": 1.1,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-day\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"alerts\": [],\n" +
                "    \"stations\": {\n" +
                "        \"SKMD\": {\n" +
                "            \"distance\": 3348.0,\n" +
                "            \"latitude\": 6.22,\n" +
                "            \"longitude\": -75.59,\n" +
                "            \"useCount\": 0,\n" +
                "            \"id\": \"SKMD\",\n" +
                "            \"name\": \"SKMD\",\n" +
                "            \"quality\": 19,\n" +
                "            \"contribution\": 0.0\n" +
                "        },\n" +
                "        \"SKRG\": {\n" +
                "            \"distance\": 17603.0,\n" +
                "            \"latitude\": 6.18,\n" +
                "            \"longitude\": -75.43,\n" +
                "            \"useCount\": 0,\n" +
                "            \"id\": \"SKRG\",\n" +
                "            \"name\": \"SKRG\",\n" +
                "            \"quality\": 49,\n" +
                "            \"contribution\": 0.0\n" +
                "        }\n" +
                "    },\n" +
                "    \"currentConditions\": {\n" +
                "        \"datetime\": \"15:00:00\",\n" +
                "        \"datetimeEpoch\": 1644436800,\n" +
                "        \"temp\": 65.9,\n" +
                "        \"feelslike\": 65.9,\n" +
                "        \"humidity\": 93.3,\n" +
                "        \"dew\": 63.9,\n" +
                "        \"precip\": 0.02,\n" +
                "        \"precipprob\": null,\n" +
                "        \"snow\": null,\n" +
                "        \"snowdepth\": 0.0,\n" +
                "        \"preciptype\": null,\n" +
                "        \"windgust\": null,\n" +
                "        \"windspeed\": 3.4,\n" +
                "        \"winddir\": 223.0,\n" +
                "        \"pressure\": 1018.2,\n" +
                "        \"visibility\": 6.2,\n" +
                "        \"cloudcover\": 88.4,\n" +
                "        \"solarradiation\": 423.0,\n" +
                "        \"solarenergy\": 1.5,\n" +
                "        \"uvindex\": 0.0,\n" +
                "        \"conditions\": \"Rain, Partially cloudy\",\n" +
                "        \"icon\": \"rain\",\n" +
                "        \"stations\": [\n" +
                "            \"SKMD\",\n" +
                "            \"SKRG\"\n" +
                "        ],\n" +
                "        \"sunrise\": \"06:19:36\",\n" +
                "        \"sunriseEpoch\": 1644405576,\n" +
                "        \"sunset\": \"18:13:33\",\n" +
                "        \"sunsetEpoch\": 1644448413,\n" +
                "        \"moonphase\": 0.29\n" +
                "    }\n" +
                "}";
    }

    private String createBigMessage(){
        return "{\n" +
                "    \"queryCost\": 1,\n" +
                "    \"latitude\": 6.24589,\n" +
                "    \"longitude\": -75.5746,\n" +
                "    \"resolvedAddress\": \"Medellín, Colombia\",\n" +
                "    \"address\": \"Medellín,ANT,CO\",\n" +
                "    \"timezone\": \"America/Bogota\",\n" +
                "    \"tzoffset\": -5.0,\n" +
                "    \"description\": \"Similar temperatures continuing with a chance of rain multiple days.\",\n" +
                "    \"days\": [\n" +
                "        {\n" +
                "            \"datetime\": \"2022-02-09\",\n" +
                "            \"datetimeEpoch\": 1644382800,\n" +
                "            \"tempmax\": 73.1,\n" +
                "            \"tempmin\": 55.3,\n" +
                "            \"temp\": 63.7,\n" +
                "            \"feelslikemax\": 73.1,\n" +
                "            \"feelslikemin\": 55.3,\n" +
                "            \"feelslike\": 63.7,\n" +
                "            \"dew\": 60.2,\n" +
                "            \"humidity\": 89.6,\n" +
                "            \"precip\": 0.32,\n" +
                "            \"precipprob\": 100.0,\n" +
                "            \"precipcover\": null,\n" +
                "            \"preciptype\": [\n" +
                "                \"rain\"\n" +
                "            ],\n" +
                "            \"snow\": null,\n" +
                "            \"snowdepth\": 0.0,\n" +
                "            \"windgust\": 8.7,\n" +
                "            \"windspeed\": 17.9,\n" +
                "            \"winddir\": 215.8,\n" +
                "            \"pressure\": 1018.2,\n" +
                "            \"cloudcover\": 88.6,\n" +
                "            \"visibility\": 7.1,\n" +
                "            \"solarradiation\": 210.8,\n" +
                "            \"solarenergy\": 16.6,\n" +
                "            \"uvindex\": 3.0,\n" +
                "            \"severerisk\": 10.0,\n" +
                "            \"sunrise\": \"06:19:36\",\n" +
                "            \"sunriseEpoch\": 1644405576,\n" +
                "            \"sunset\": \"18:13:33\",\n" +
                "            \"sunsetEpoch\": 1644448413,\n" +
                "            \"moonphase\": 0.29,\n" +
                "            \"conditions\": \"Rain, Partially cloudy\",\n" +
                "            \"description\": \"Partly cloudy throughout the day with rain.\",\n" +
                "            \"icon\": \"rain\",\n" +
                "            \"stations\": [\n" +
                "                \"SKMD\",\n" +
                "                \"SKRG\"\n" +
                "            ],\n" +
                "            \"source\": \"comb\",\n" +
                "            \"hours\": [\n" +
                "                {\n" +
                "                    \"datetime\": \"00:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644382800,\n" +
                "                    \"temp\": 57.1,\n" +
                "                    \"feelslike\": 57.1,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 57.1,\n" +
                "                    \"precip\": null,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 1.6,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1026.0,\n" +
                "                    \"visibility\": 4.3,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-night\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"01:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644386400,\n" +
                "                    \"temp\": 57.1,\n" +
                "                    \"feelslike\": 57.1,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 57.1,\n" +
                "                    \"precip\": null,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 1.8,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1026.0,\n" +
                "                    \"visibility\": 3.7,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": null,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-night\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"02:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644390000,\n" +
                "                    \"temp\": 57.1,\n" +
                "                    \"feelslike\": 57.1,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 57.1,\n" +
                "                    \"precip\": null,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 1.8,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1024.0,\n" +
                "                    \"visibility\": 3.7,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-night\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"03:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644393600,\n" +
                "                    \"temp\": 57.1,\n" +
                "                    \"feelslike\": 57.1,\n" +
                "                    \"humidity\": 93.7,\n" +
                "                    \"dew\": 55.3,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.8,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1024.0,\n" +
                "                    \"visibility\": 5.0,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-night\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"04:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644397200,\n" +
                "                    \"temp\": 55.3,\n" +
                "                    \"feelslike\": 55.3,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 55.3,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.8,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1023.0,\n" +
                "                    \"visibility\": 3.7,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-night\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"05:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644400800,\n" +
                "                    \"temp\": 55.3,\n" +
                "                    \"feelslike\": 55.3,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 55.3,\n" +
                "                    \"precip\": 0.01,\n" +
                "                    \"precipprob\": 100.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 2.0,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1024.0,\n" +
                "                    \"visibility\": 3.7,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Partially cloudy\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"06:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644404400,\n" +
                "                    \"temp\": 64.0,\n" +
                "                    \"feelslike\": 64.0,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 64.0,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 2.0,\n" +
                "                    \"windspeed\": 4.6,\n" +
                "                    \"winddir\": 320.0,\n" +
                "                    \"pressure\": 1017.2,\n" +
                "                    \"visibility\": 4.3,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-night\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"07:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644408000,\n" +
                "                    \"temp\": 64.0,\n" +
                "                    \"feelslike\": 64.0,\n" +
                "                    \"humidity\": 100.0,\n" +
                "                    \"dew\": 64.0,\n" +
                "                    \"precip\": null,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 2.0,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1020.2,\n" +
                "                    \"visibility\": 4.8,\n" +
                "                    \"cloudcover\": 88.4,\n" +
                "                    \"solarradiation\": null,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-day\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"08:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644411600,\n" +
                "                    \"temp\": 67.6,\n" +
                "                    \"feelslike\": 67.6,\n" +
                "                    \"humidity\": 83.21,\n" +
                "                    \"dew\": 62.3,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.6,\n" +
                "                    \"windspeed\": 0.1,\n" +
                "                    \"winddir\": 0.0,\n" +
                "                    \"pressure\": 1020.2,\n" +
                "                    \"visibility\": 4.3,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 208.0,\n" +
                "                    \"solarenergy\": 0.7,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-day\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"09:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644415200,\n" +
                "                    \"temp\": 69.5,\n" +
                "                    \"feelslike\": 69.5,\n" +
                "                    \"humidity\": 77.89,\n" +
                "                    \"dew\": 62.3,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.6,\n" +
                "                    \"windspeed\": 4.7,\n" +
                "                    \"winddir\": 22.0,\n" +
                "                    \"pressure\": 1021.2,\n" +
                "                    \"visibility\": 4.4,\n" +
                "                    \"cloudcover\": 86.7,\n" +
                "                    \"solarradiation\": 316.0,\n" +
                "                    \"solarenergy\": 1.1,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-day\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"10:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644418800,\n" +
                "                    \"temp\": 71.3,\n" +
                "                    \"feelslike\": 71.3,\n" +
                "                    \"humidity\": 68.74,\n" +
                "                    \"dew\": 60.5,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 2.5,\n" +
                "                    \"windspeed\": 9.1,\n" +
                "                    \"winddir\": 342.0,\n" +
                "                    \"pressure\": 1022.1,\n" +
                "                    \"visibility\": 5.6,\n" +
                "                    \"cloudcover\": 86.7,\n" +
                "                    \"solarradiation\": 419.0,\n" +
                "                    \"solarenergy\": 1.5,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-day\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"11:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644422400,\n" +
                "                    \"temp\": 73.1,\n" +
                "                    \"feelslike\": 73.1,\n" +
                "                    \"humidity\": 64.68,\n" +
                "                    \"dew\": 60.5,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 0.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 3.6,\n" +
                "                    \"windspeed\": 15.6,\n" +
                "                    \"winddir\": 350.0,\n" +
                "                    \"pressure\": 1021.2,\n" +
                "                    \"visibility\": 6.2,\n" +
                "                    \"cloudcover\": 88.0,\n" +
                "                    \"solarradiation\": 523.0,\n" +
                "                    \"solarenergy\": 1.9,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Partially cloudy\",\n" +
                "                    \"icon\": \"partly-cloudy-day\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"12:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644426000,\n" +
                "                    \"temp\": 73.0,\n" +
                "                    \"feelslike\": 73.0,\n" +
                "                    \"humidity\": 64.96,\n" +
                "                    \"dew\": 60.5,\n" +
                "                    \"precip\": 0.01,\n" +
                "                    \"precipprob\": 100.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 3.8,\n" +
                "                    \"windspeed\": 15.9,\n" +
                "                    \"winddir\": 342.0,\n" +
                "                    \"pressure\": 1020.2,\n" +
                "                    \"visibility\": 6.2,\n" +
                "                    \"cloudcover\": 51.3,\n" +
                "                    \"solarradiation\": 607.0,\n" +
                "                    \"solarenergy\": 2.2,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Partially cloudy\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"13:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644429600,\n" +
                "                    \"temp\": 71.1,\n" +
                "                    \"feelslike\": 71.1,\n" +
                "                    \"humidity\": 69.34,\n" +
                "                    \"dew\": 60.6,\n" +
                "                    \"precip\": 0.01,\n" +
                "                    \"precipprob\": 100.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 3.4,\n" +
                "                    \"windspeed\": 17.9,\n" +
                "                    \"winddir\": 11.0,\n" +
                "                    \"pressure\": 1019.2,\n" +
                "                    \"visibility\": 6.1,\n" +
                "                    \"cloudcover\": 51.3,\n" +
                "                    \"solarradiation\": 642.0,\n" +
                "                    \"solarenergy\": 2.3,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Partially cloudy\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"14:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644433200,\n" +
                "                    \"temp\": 64.2,\n" +
                "                    \"feelslike\": 64.2,\n" +
                "                    \"humidity\": 93.26,\n" +
                "                    \"dew\": 62.2,\n" +
                "                    \"precip\": 0.01,\n" +
                "                    \"precipprob\": 100.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.8,\n" +
                "                    \"windspeed\": 4.7,\n" +
                "                    \"winddir\": 332.0,\n" +
                "                    \"pressure\": 1019.2,\n" +
                "                    \"visibility\": 5.0,\n" +
                "                    \"cloudcover\": 88.4,\n" +
                "                    \"solarradiation\": 528.0,\n" +
                "                    \"solarenergy\": 1.9,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Partially cloudy\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"15:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644436800,\n" +
                "                    \"temp\": 65.9,\n" +
                "                    \"feelslike\": 65.9,\n" +
                "                    \"humidity\": 93.31,\n" +
                "                    \"dew\": 63.9,\n" +
                "                    \"precip\": 0.02,\n" +
                "                    \"precipprob\": null,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 2.7,\n" +
                "                    \"windspeed\": 3.4,\n" +
                "                    \"winddir\": 223.0,\n" +
                "                    \"pressure\": 1018.2,\n" +
                "                    \"visibility\": 6.2,\n" +
                "                    \"cloudcover\": 88.4,\n" +
                "                    \"solarradiation\": 423.0,\n" +
                "                    \"solarenergy\": 1.5,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Partially cloudy\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": [\n" +
                "                        \"SKMD\",\n" +
                "                        \"SKRG\"\n" +
                "                    ],\n" +
                "                    \"source\": \"obs\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"16:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644440400,\n" +
                "                    \"temp\": 66.8,\n" +
                "                    \"feelslike\": 66.8,\n" +
                "                    \"humidity\": 82.79,\n" +
                "                    \"dew\": 61.4,\n" +
                "                    \"precip\": 0.04,\n" +
                "                    \"precipprob\": null,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 5.6,\n" +
                "                    \"windspeed\": 3.8,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1008.0,\n" +
                "                    \"visibility\": 10.8,\n" +
                "                    \"cloudcover\": 96.2,\n" +
                "                    \"solarradiation\": 328.0,\n" +
                "                    \"solarenergy\": 1.2,\n" +
                "                    \"uvindex\": 3.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"17:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644444000,\n" +
                "                    \"temp\": 64.5,\n" +
                "                    \"feelslike\": 64.5,\n" +
                "                    \"humidity\": 90.96,\n" +
                "                    \"dew\": 61.8,\n" +
                "                    \"precip\": 0.02,\n" +
                "                    \"precipprob\": null,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 4.0,\n" +
                "                    \"windspeed\": 2.7,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1009.0,\n" +
                "                    \"visibility\": 14.4,\n" +
                "                    \"cloudcover\": 97.7,\n" +
                "                    \"solarradiation\": 258.0,\n" +
                "                    \"solarenergy\": 0.9,\n" +
                "                    \"uvindex\": 3.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"18:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644447600,\n" +
                "                    \"temp\": 63.9,\n" +
                "                    \"feelslike\": 63.9,\n" +
                "                    \"humidity\": 90.36,\n" +
                "                    \"dew\": 61.1,\n" +
                "                    \"precip\": 0.06,\n" +
                "                    \"precipprob\": null,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 2.5,\n" +
                "                    \"windspeed\": 2.0,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1010.0,\n" +
                "                    \"visibility\": 5.4,\n" +
                "                    \"cloudcover\": 99.1,\n" +
                "                    \"solarradiation\": 210.0,\n" +
                "                    \"solarenergy\": 0.8,\n" +
                "                    \"uvindex\": 2.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"19:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644451200,\n" +
                "                    \"temp\": 63.1,\n" +
                "                    \"feelslike\": 63.1,\n" +
                "                    \"humidity\": 92.66,\n" +
                "                    \"dew\": 60.9,\n" +
                "                    \"precip\": 0.05,\n" +
                "                    \"precipprob\": 100.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 2.7,\n" +
                "                    \"windspeed\": 2.5,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1011.0,\n" +
                "                    \"visibility\": 9.1,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 175.0,\n" +
                "                    \"solarenergy\": 0.6,\n" +
                "                    \"uvindex\": 2.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"20:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644454800,\n" +
                "                    \"temp\": 62.3,\n" +
                "                    \"feelslike\": 62.3,\n" +
                "                    \"humidity\": 94.43,\n" +
                "                    \"dew\": 60.7,\n" +
                "                    \"precip\": 0.02,\n" +
                "                    \"precipprob\": 57.1,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 8.7,\n" +
                "                    \"windspeed\": 4.0,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1012.0,\n" +
                "                    \"visibility\": 14.4,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"21:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644458400,\n" +
                "                    \"temp\": 61.6,\n" +
                "                    \"feelslike\": 61.6,\n" +
                "                    \"humidity\": 96.25,\n" +
                "                    \"dew\": 60.5,\n" +
                "                    \"precip\": 0.02,\n" +
                "                    \"precipprob\": 57.1,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 6.7,\n" +
                "                    \"windspeed\": 2.7,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1013.0,\n" +
                "                    \"visibility\": 12.7,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"22:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644462000,\n" +
                "                    \"temp\": 61.6,\n" +
                "                    \"feelslike\": 61.6,\n" +
                "                    \"humidity\": 96.86,\n" +
                "                    \"dew\": 60.7,\n" +
                "                    \"precip\": 0.02,\n" +
                "                    \"precipprob\": 57.1,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 0.9,\n" +
                "                    \"windspeed\": 0.9,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1014.0,\n" +
                "                    \"visibility\": 11.8,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"23:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644465600,\n" +
                "                    \"temp\": 61.4,\n" +
                "                    \"feelslike\": 61.4,\n" +
                "                    \"humidity\": 96.86,\n" +
                "                    \"dew\": 60.5,\n" +
                "                    \"precip\": 0.03,\n" +
                "                    \"precipprob\": 57.1,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 2.0,\n" +
                "                    \"windspeed\": 1.6,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1014.0,\n" +
                "                    \"visibility\": 13.9,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"datetime\": \"2022-02-10\",\n" +
                "            \"datetimeEpoch\": 1644469200,\n" +
                "            \"tempmax\": 77.1,\n" +
                "            \"tempmin\": 59.8,\n" +
                "            \"temp\": 65.7,\n" +
                "            \"feelslikemax\": 77.1,\n" +
                "            \"feelslikemin\": 59.8,\n" +
                "            \"feelslike\": 65.7,\n" +
                "            \"dew\": 59.8,\n" +
                "            \"humidity\": 83.3,\n" +
                "            \"precip\": 0.35,\n" +
                "            \"precipprob\": 100.0,\n" +
                "            \"precipcover\": null,\n" +
                "            \"preciptype\": [\n" +
                "                \"rain\"\n" +
                "            ],\n" +
                "            \"snow\": null,\n" +
                "            \"snowdepth\": 0.0,\n" +
                "            \"windgust\": 5.1,\n" +
                "            \"windspeed\": 2.9,\n" +
                "            \"winddir\": null,\n" +
                "            \"pressure\": 1011.8,\n" +
                "            \"cloudcover\": 96.5,\n" +
                "            \"visibility\": 12.8,\n" +
                "            \"solarradiation\": 231.3,\n" +
                "            \"solarenergy\": 19.9,\n" +
                "            \"uvindex\": 9.0,\n" +
                "            \"severerisk\": 30.0,\n" +
                "            \"sunrise\": \"06:19:29\",\n" +
                "            \"sunriseEpoch\": 1644491969,\n" +
                "            \"sunset\": \"18:13:42\",\n" +
                "            \"sunsetEpoch\": 1644534822,\n" +
                "            \"moonphase\": 0.34,\n" +
                "            \"conditions\": \"Rain, Overcast\",\n" +
                "            \"description\": \"Cloudy skies throughout the day with storms possible.\",\n" +
                "            \"icon\": \"rain\",\n" +
                "            \"stations\": null,\n" +
                "            \"source\": \"fcst\",\n" +
                "            \"hours\": [\n" +
                "                {\n" +
                "                    \"datetime\": \"00:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644469200,\n" +
                "                    \"temp\": 61.4,\n" +
                "                    \"feelslike\": 61.4,\n" +
                "                    \"humidity\": 96.86,\n" +
                "                    \"dew\": 60.5,\n" +
                "                    \"precip\": 0.03,\n" +
                "                    \"precipprob\": 57.1,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 3.1,\n" +
                "                    \"windspeed\": 1.8,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1014.0,\n" +
                "                    \"visibility\": 14.1,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"01:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644472800,\n" +
                "                    \"temp\": 61.1,\n" +
                "                    \"feelslike\": 61.1,\n" +
                "                    \"humidity\": 97.48,\n" +
                "                    \"dew\": 60.3,\n" +
                "                    \"precip\": 0.02,\n" +
                "                    \"precipprob\": 57.1,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 3.4,\n" +
                "                    \"windspeed\": 1.8,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1013.0,\n" +
                "                    \"visibility\": 12.9,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"02:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644476400,\n" +
                "                    \"temp\": 60.9,\n" +
                "                    \"feelslike\": 60.9,\n" +
                "                    \"humidity\": 97.48,\n" +
                "                    \"dew\": 60.2,\n" +
                "                    \"precip\": 0.04,\n" +
                "                    \"precipprob\": 19.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 3.1,\n" +
                "                    \"windspeed\": 1.8,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1013.0,\n" +
                "                    \"visibility\": 8.5,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Overcast\",\n" +
                "                    \"icon\": \"cloudy\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"03:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644480000,\n" +
                "                    \"temp\": 60.5,\n" +
                "                    \"feelslike\": 60.5,\n" +
                "                    \"humidity\": 97.47,\n" +
                "                    \"dew\": 59.8,\n" +
                "                    \"precip\": 0.05,\n" +
                "                    \"precipprob\": 19.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 5.1,\n" +
                "                    \"windspeed\": 2.5,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1013.0,\n" +
                "                    \"visibility\": 1.1,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Overcast\",\n" +
                "                    \"icon\": \"cloudy\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"04:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644483600,\n" +
                "                    \"temp\": 60.2,\n" +
                "                    \"feelslike\": 60.2,\n" +
                "                    \"humidity\": 98.1,\n" +
                "                    \"dew\": 59.6,\n" +
                "                    \"precip\": 0.04,\n" +
                "                    \"precipprob\": 19.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 3.1,\n" +
                "                    \"windspeed\": 1.3,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1013.0,\n" +
                "                    \"visibility\": 1.1,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Overcast\",\n" +
                "                    \"icon\": \"cloudy\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"05:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644487200,\n" +
                "                    \"temp\": 60.2,\n" +
                "                    \"feelslike\": 60.2,\n" +
                "                    \"humidity\": 97.47,\n" +
                "                    \"dew\": 59.4,\n" +
                "                    \"precip\": 0.01,\n" +
                "                    \"precipprob\": 19.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.3,\n" +
                "                    \"windspeed\": 1.1,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1013.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Overcast\",\n" +
                "                    \"icon\": \"cloudy\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"06:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644490800,\n" +
                "                    \"temp\": 59.8,\n" +
                "                    \"feelslike\": 59.8,\n" +
                "                    \"humidity\": 97.46,\n" +
                "                    \"dew\": 59.1,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 19.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.1,\n" +
                "                    \"windspeed\": 1.1,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1014.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Overcast\",\n" +
                "                    \"icon\": \"cloudy\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"07:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644494400,\n" +
                "                    \"temp\": 60.3,\n" +
                "                    \"feelslike\": 60.3,\n" +
                "                    \"humidity\": 96.23,\n" +
                "                    \"dew\": 59.3,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 19.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.1,\n" +
                "                    \"windspeed\": 1.1,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1014.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 3.0,\n" +
                "                    \"solarenergy\": 0.0,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Overcast\",\n" +
                "                    \"icon\": \"cloudy\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"08:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644498000,\n" +
                "                    \"temp\": 62.7,\n" +
                "                    \"feelslike\": 62.7,\n" +
                "                    \"humidity\": 91.47,\n" +
                "                    \"dew\": 60.2,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 76.2,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.6,\n" +
                "                    \"windspeed\": 0.9,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1014.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 119.0,\n" +
                "                    \"solarenergy\": 0.4,\n" +
                "                    \"uvindex\": 1.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"09:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644501600,\n" +
                "                    \"temp\": 64.3,\n" +
                "                    \"feelslike\": 64.3,\n" +
                "                    \"humidity\": 88.1,\n" +
                "                    \"dew\": 60.7,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 76.2,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.6,\n" +
                "                    \"windspeed\": 1.1,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1015.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 162.0,\n" +
                "                    \"solarenergy\": 0.6,\n" +
                "                    \"uvindex\": 2.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"10:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644505200,\n" +
                "                    \"temp\": 67.7,\n" +
                "                    \"feelslike\": 67.7,\n" +
                "                    \"humidity\": 79.25,\n" +
                "                    \"dew\": 61.1,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 76.2,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.3,\n" +
                "                    \"windspeed\": 1.8,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1014.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 254.0,\n" +
                "                    \"solarenergy\": 0.9,\n" +
                "                    \"uvindex\": 3.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"11:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644508800,\n" +
                "                    \"temp\": 70.6,\n" +
                "                    \"feelslike\": 70.6,\n" +
                "                    \"humidity\": 70.0,\n" +
                "                    \"dew\": 60.3,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 76.2,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 2.7,\n" +
                "                    \"windspeed\": 2.7,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1013.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 99.8,\n" +
                "                    \"solarradiation\": 333.0,\n" +
                "                    \"solarenergy\": 1.2,\n" +
                "                    \"uvindex\": 3.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"12:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644512400,\n" +
                "                    \"temp\": 72.2,\n" +
                "                    \"feelslike\": 72.2,\n" +
                "                    \"humidity\": 65.0,\n" +
                "                    \"dew\": 59.8,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 76.2,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 3.4,\n" +
                "                    \"windspeed\": 2.9,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1012.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 88.8,\n" +
                "                    \"solarradiation\": 398.0,\n" +
                "                    \"solarenergy\": 1.4,\n" +
                "                    \"uvindex\": 4.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Partially cloudy\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"13:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644516000,\n" +
                "                    \"temp\": 75.1,\n" +
                "                    \"feelslike\": 75.1,\n" +
                "                    \"humidity\": 58.63,\n" +
                "                    \"dew\": 59.6,\n" +
                "                    \"precip\": 0.01,\n" +
                "                    \"precipprob\": 76.2,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 3.4,\n" +
                "                    \"windspeed\": 2.7,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1010.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 84.1,\n" +
                "                    \"solarradiation\": 472.0,\n" +
                "                    \"solarenergy\": 1.7,\n" +
                "                    \"uvindex\": 5.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Partially cloudy\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"14:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644519600,\n" +
                "                    \"temp\": 76.7,\n" +
                "                    \"feelslike\": 76.7,\n" +
                "                    \"humidity\": 54.14,\n" +
                "                    \"dew\": 58.9,\n" +
                "                    \"precip\": 0.01,\n" +
                "                    \"precipprob\": 100.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 3.1,\n" +
                "                    \"windspeed\": 2.5,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1008.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 85.9,\n" +
                "                    \"solarradiation\": 853.0,\n" +
                "                    \"solarenergy\": 3.1,\n" +
                "                    \"uvindex\": 9.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Partially cloudy\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"15:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644523200,\n" +
                "                    \"temp\": 77.1,\n" +
                "                    \"feelslike\": 77.1,\n" +
                "                    \"humidity\": 51.8,\n" +
                "                    \"dew\": 58.0,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 100.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 3.1,\n" +
                "                    \"windspeed\": 2.5,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1007.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 75.7,\n" +
                "                    \"solarradiation\": 788.0,\n" +
                "                    \"solarenergy\": 2.8,\n" +
                "                    \"uvindex\": 8.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Partially cloudy\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"16:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644526800,\n" +
                "                    \"temp\": 76.0,\n" +
                "                    \"feelslike\": 76.0,\n" +
                "                    \"humidity\": 53.0,\n" +
                "                    \"dew\": 57.6,\n" +
                "                    \"precip\": 0.02,\n" +
                "                    \"precipprob\": 100.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 3.1,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1007.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 81.6,\n" +
                "                    \"solarradiation\": 690.0,\n" +
                "                    \"solarenergy\": 2.5,\n" +
                "                    \"uvindex\": 7.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Partially cloudy\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"17:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644530400,\n" +
                "                    \"temp\": 73.3,\n" +
                "                    \"feelslike\": 73.3,\n" +
                "                    \"humidity\": 65.12,\n" +
                "                    \"dew\": 60.9,\n" +
                "                    \"precip\": 0.01,\n" +
                "                    \"precipprob\": 100.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 4.0,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1007.0,\n" +
                "                    \"visibility\": 14.2,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 588.0,\n" +
                "                    \"solarenergy\": 2.1,\n" +
                "                    \"uvindex\": 6.0,\n" +
                "                    \"severerisk\": 30.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"18:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644534000,\n" +
                "                    \"temp\": 66.1,\n" +
                "                    \"feelslike\": 66.1,\n" +
                "                    \"humidity\": 81.7,\n" +
                "                    \"dew\": 60.3,\n" +
                "                    \"precip\": 0.03,\n" +
                "                    \"precipprob\": 100.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 1.8,\n" +
                "                    \"windspeed\": 2.2,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1009.0,\n" +
                "                    \"visibility\": 7.9,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 486.0,\n" +
                "                    \"solarenergy\": 1.7,\n" +
                "                    \"uvindex\": 5.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"19:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644537600,\n" +
                "                    \"temp\": 62.9,\n" +
                "                    \"feelslike\": 62.9,\n" +
                "                    \"humidity\": 90.32,\n" +
                "                    \"dew\": 60.0,\n" +
                "                    \"precip\": 0.04,\n" +
                "                    \"precipprob\": 100.0,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": [\n" +
                "                        \"rain\"\n" +
                "                    ],\n" +
                "                    \"windgust\": 1.8,\n" +
                "                    \"windspeed\": 2.0,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1011.0,\n" +
                "                    \"visibility\": 8.1,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 405.0,\n" +
                "                    \"solarenergy\": 1.5,\n" +
                "                    \"uvindex\": 4.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"20:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644541200,\n" +
                "                    \"temp\": 62.3,\n" +
                "                    \"feelslike\": 62.3,\n" +
                "                    \"humidity\": 92.05,\n" +
                "                    \"dew\": 60.0,\n" +
                "                    \"precip\": 0.03,\n" +
                "                    \"precipprob\": 52.4,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.6,\n" +
                "                    \"windspeed\": 1.8,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1011.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"21:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644544800,\n" +
                "                    \"temp\": 62.3,\n" +
                "                    \"feelslike\": 62.3,\n" +
                "                    \"humidity\": 92.64,\n" +
                "                    \"dew\": 60.2,\n" +
                "                    \"precip\": 0.01,\n" +
                "                    \"precipprob\": 52.4,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.3,\n" +
                "                    \"windspeed\": 1.3,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1012.0,\n" +
                "                    \"visibility\": 14.4,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"22:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644548400,\n" +
                "                    \"temp\": 62.1,\n" +
                "                    \"feelslike\": 62.1,\n" +
                "                    \"humidity\": 93.23,\n" +
                "                    \"dew\": 60.2,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 52.4,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 0.9,\n" +
                "                    \"windspeed\": 1.1,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1013.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"datetime\": \"23:00:00\",\n" +
                "                    \"datetimeEpoch\": 1644552000,\n" +
                "                    \"temp\": 61.8,\n" +
                "                    \"feelslike\": 61.8,\n" +
                "                    \"humidity\": 94.42,\n" +
                "                    \"dew\": 60.2,\n" +
                "                    \"precip\": 0.0,\n" +
                "                    \"precipprob\": 52.4,\n" +
                "                    \"snow\": null,\n" +
                "                    \"snowdepth\": 0.0,\n" +
                "                    \"preciptype\": null,\n" +
                "                    \"windgust\": 1.3,\n" +
                "                    \"windspeed\": 0.9,\n" +
                "                    \"winddir\": null,\n" +
                "                    \"pressure\": 1013.0,\n" +
                "                    \"visibility\": 15.0,\n" +
                "                    \"cloudcover\": 100.0,\n" +
                "                    \"solarradiation\": 0.0,\n" +
                "                    \"solarenergy\": null,\n" +
                "                    \"uvindex\": 0.0,\n" +
                "                    \"severerisk\": 10.0,\n" +
                "                    \"conditions\": \"Rain, Overcast\",\n" +
                "                    \"icon\": \"rain\",\n" +
                "                    \"stations\": null,\n" +
                "                    \"source\": \"fcst\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"alerts\": [],\n" +
                "    \"stations\": {\n" +
                "        \"SKMD\": {\n" +
                "            \"distance\": 3348.0,\n" +
                "            \"latitude\": 6.22,\n" +
                "            \"longitude\": -75.59,\n" +
                "            \"useCount\": 0,\n" +
                "            \"id\": \"SKMD\",\n" +
                "            \"name\": \"SKMD\",\n" +
                "            \"quality\": 19,\n" +
                "            \"contribution\": 0.0\n" +
                "        },\n" +
                "        \"SKRG\": {\n" +
                "            \"distance\": 17603.0,\n" +
                "            \"latitude\": 6.18,\n" +
                "            \"longitude\": -75.43,\n" +
                "            \"useCount\": 0,\n" +
                "            \"id\": \"SKRG\",\n" +
                "            \"name\": \"SKRG\",\n" +
                "            \"quality\": 49,\n" +
                "            \"contribution\": 0.0\n" +
                "        }\n" +
                "    },\n" +
                "    \"currentConditions\": {\n" +
                "        \"datetime\": \"15:00:00\",\n" +
                "        \"datetimeEpoch\": 1644436800,\n" +
                "        \"temp\": 65.9,\n" +
                "        \"feelslike\": 65.9,\n" +
                "        \"humidity\": 93.3,\n" +
                "        \"dew\": 63.9,\n" +
                "        \"precip\": 0.02,\n" +
                "        \"precipprob\": null,\n" +
                "        \"snow\": null,\n" +
                "        \"snowdepth\": 0.0,\n" +
                "        \"preciptype\": null,\n" +
                "        \"windgust\": null,\n" +
                "        \"windspeed\": 3.4,\n" +
                "        \"winddir\": 223.0,\n" +
                "        \"pressure\": 1018.2,\n" +
                "        \"visibility\": 6.2,\n" +
                "        \"cloudcover\": 88.4,\n" +
                "        \"solarradiation\": 423.0,\n" +
                "        \"solarenergy\": 1.5,\n" +
                "        \"uvindex\": 0.0,\n" +
                "        \"conditions\": \"Rain, Partially cloudy\",\n" +
                "        \"icon\": \"rain\",\n" +
                "        \"stations\": [\n" +
                "            \"SKMD\",\n" +
                "            \"SKRG\"\n" +
                "        ],\n" +
                "        \"sunrise\": \"06:19:36\",\n" +
                "        \"sunriseEpoch\": 1644405576,\n" +
                "        \"sunset\": \"18:13:33\",\n" +
                "        \"sunsetEpoch\": 1644448413,\n" +
                "        \"moonphase\": 0.29\n" +
                "    }\n" +
                "}";
    }
}
