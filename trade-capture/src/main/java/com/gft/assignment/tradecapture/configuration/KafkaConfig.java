package com.gft.assignment.tradecapture.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;
import java.util.HashMap;

@Configuration
public class KafkaConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.kafka.admin")
    public Map<String, String> kafkaAdminConfigMap() {
        return new HashMap<>();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.kafka.producer")
    public Map<String, String> kafkaProducerConfigMap() {
        return new HashMap<>();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.kafka.topics")
    public Map<String, String> kafkaTopics() {
        return new HashMap<>();
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>(kafkaAdminConfigMap());
        return new KafkaAdmin(configs);
    }

    //TODO how to do this better
    @Bean
    public NewTopic tradeRecordsTopic() {
        return new NewTopic("trade-records", 1, (short) 1);
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>(kafkaProducerConfigMap());
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ?> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
