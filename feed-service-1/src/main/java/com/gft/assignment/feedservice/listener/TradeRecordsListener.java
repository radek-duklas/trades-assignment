package com.gft.assignment.feedservice.listener;

import com.gft.assignment.tradecapture.model.TradeRecordProcessed;
import com.gft.assignment.feedservice.service.CollectionConsumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

//TODO check

@Slf4j
@Component
@AllArgsConstructor
public class TradeRecordsListener {
    private CollectionConsumer<TradeRecordProcessed> consumer;

    @KafkaListener(topics = "${app.kafka.topic}")
    public void listen(List<TradeRecordProcessed> tradeRecord) {
        log.info("Received {} records", tradeRecord.size());
        consumer.accept(tradeRecord);
    }
}
