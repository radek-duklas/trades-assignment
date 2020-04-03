package com.gft.assignment.tradecapture.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gft.assignment.tradecapture.converter.Converter;
import com.gft.assignment.tradecapture.model.TradeRecord;
import com.gft.assignment.tradecapture.model.TradeRecordProcessed;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.net.URL;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeRecordKafkaConsumerTest {

    private TradeRecordKafkaConsumer consumer;

    @Test
    void shouldConsumeAllDataFromIterator() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerFor(TradeRecord.class);
        URL jsonUrl = Objects.requireNonNull(this.getClass().getClassLoader().getResource("data/input.json"));
        MappingIterator<TradeRecord> objectMappingIterator = reader.readValues(jsonUrl);
        Converter<TradeRecord, TradeRecordProcessed> converter = mock(Converter.class);
        KafkaTemplate kafkaTemplate = mock(KafkaTemplate.class);
        String topic = "topic";
        consumer = new TradeRecordKafkaConsumer(kafkaTemplate, converter, topic);
        consumer.consumeNewData(objectMappingIterator);
        verify(kafkaTemplate, times(3)).send(eq(topic), any());
        verify(converter, times(3)).convert(any());
        verify(converter).init();
    }
}