package com.gft.assignment.tradecapture.configuration;

import com.gft.assignment.tradecapture.converter.Converter;
import com.gft.assignment.tradecapture.model.TradeRecord;
import com.gft.assignment.tradecapture.model.TradeRecordProcessed;
import com.gft.assignment.tradecapture.service.DataCollectionConsumer;
import com.gft.assignment.tradecapture.service.TradeRecordKafkaConsumer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class BeanConfiguration {

    @Bean
    DataCollectionConsumer<TradeRecord> tradeRecordDataCollectionConsumer(KafkaConfig kafkaConfig,
            KafkaTemplate<String, TradeRecordProcessed> kafkaTemplate,
            Converter<TradeRecord, TradeRecordProcessed> converter) {
        return new TradeRecordKafkaConsumer(kafkaTemplate, converter,
                kafkaConfig.kafkaTopics().get(TradeRecordProcessed.class.getSimpleName()));
    }

    @Bean
    ExecutorService executorService() {
        return Executors.newSingleThreadExecutor();
    }
}
