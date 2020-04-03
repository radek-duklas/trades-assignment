package com.gft.assignment.tradecapture.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.assignment.tradecapture.TestUtils;
import com.gft.assignment.tradecapture.converter.Converter;
import com.gft.assignment.tradecapture.model.TradeRecord;
import com.gft.assignment.tradecapture.model.TradeRecordProcessed;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeRecordKafkaConsumerTest {

    private TradeRecordKafkaConsumer consumer;

    @Test
    void shouldConsumeAllDataFromIterator() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MappingIterator<TradeRecord> objectMappingIterator = TestUtils.iteratorFromJsonArray(objectMapper, "data/input.json");
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