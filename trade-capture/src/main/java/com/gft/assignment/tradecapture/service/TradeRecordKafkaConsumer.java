package com.gft.assignment.tradecapture.service;

import com.gft.assignment.tradecapture.converter.Converter;
import com.gft.assignment.tradecapture.model.TradeRecord;
import com.gft.assignment.tradecapture.model.TradeRecordProcessed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Iterator;

@Slf4j
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
            TradeRecord tradeRecord = iterator.next();
            log.debug("Consuming {}", tradeRecord);
            kafkaTemplate.send(kafkaTopic, converter.convert(tradeRecord));
        }
    }

    @Override
    public void consumeReplacingData(Iterator<TradeRecord> iterator) throws Exception {
        //TODO needed?
    }
}
