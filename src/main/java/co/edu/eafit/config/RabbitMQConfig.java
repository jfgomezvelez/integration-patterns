package co.edu.eafit.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

//    @Bean
//    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
//        container.setPrefetchCount(60);
//        container.setMaxConcurrentConsumers(5);
//        container.setConcurrentConsumers(3);
//        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
//        return container;
//    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> simpleMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory container = new SimpleRabbitListenerContainerFactory();
        container.setConnectionFactory(connectionFactory);
        container.setPrefetchCount(60);
        container.setMaxConcurrentConsumers(5);
        container.setConcurrentConsumers(3);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return container;
    }
}
