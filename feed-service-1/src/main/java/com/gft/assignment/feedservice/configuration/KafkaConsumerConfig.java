package com.gft.assignment.feedservice.configuration;

import com.gft.assignment.feedservice.model.TradeRecordProcessed;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;
import java.util.HashMap;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.kafka.consumer")
    public Map<String, String> kafkaConsumerConfigMap() {
        return new HashMap<>();
    }

    @Bean
    public ConsumerFactory<String, TradeRecordProcessed> consumerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaConsumerConfigMap());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TradeRecordProcessed> kafkaListenerContainerFactory(
            @Value("${app.kafka.idle-between-polls}") long idleBetweenPolls) {
        ConcurrentKafkaListenerContainerFactory<String, TradeRecordProcessed> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.getContainerProperties().setIdleBetweenPolls(idleBetweenPolls);
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        return factory;
    }
}
