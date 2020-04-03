package com.gft.assignment.tradecapture.service;

import com.gft.assignment.tradecapture.converter.Converter;
import com.gft.assignment.tradecapture.model.TradeRecord;
import com.gft.assignment.tradecapture.model.TradeRecordProcessed;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Iterator;

public class TradeRecordKafkaConsumer implements DataCollectionConsumer<TradeRecord> {

    private KafkaTemplate<String, TradeRecordProcessed> kafkaTemplate;

    private Converter<TradeRecord, TradeRecordProcessed> converter;

    private String kafkaTopic;

    public TradeRecordKafkaConsumer(KafkaTemplate<String, TradeRecordProcessed> kafkaTemplate,
                                    Converter<TradeRecord, TradeRecordProcessed> converter,
                                    String kafkaTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.converter = converter;
        this.kafkaTopic = kafkaTopic;
    }

    @Override
    public void consumeNewData(Iterator<TradeRecord> iterator) throws Exception {
        converter.init();
        while (iterator.hasNext()) {
            kafkaTemplate.send(kafkaTopic, converter.convert(iterator.next()));
        }
    }

    @Override
    public void consumeReplacingData(Iterator<TradeRecord> iterator) throws Exception {
        //TODO needed?
    }
}
