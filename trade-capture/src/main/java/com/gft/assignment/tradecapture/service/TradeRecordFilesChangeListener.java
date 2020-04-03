package com.gft.assignment.tradecapture.service;

import com.gft.assignment.tradecapture.model.TradeRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Slf4j
@Component
@AllArgsConstructor
public class TradeRecordFilesChangeListener implements FileChangeListener {

    private DataCollectionConsumer<TradeRecord> consumer;

    @Override
    public void fileCreated(Path path) {
        JSONArrayIteratorFactory<TradeRecord> arrayIteratorFactory
                = new JSONArrayIteratorFactory<>(path.toFile(), TradeRecord.class);
        try {
            consumer.consumeNewData(arrayIteratorFactory.iterator());
        } catch (Exception e) {
            log.error("Unable to process data from file {}", path, e);
        }
    }

    @Override
    public void fileModified(Path path) {
        JSONArrayIteratorFactory<TradeRecord> arrayIteratorFactory
                = new JSONArrayIteratorFactory<>(path.toFile(), TradeRecord.class);
        try {
            consumer.consumeReplacingData(arrayIteratorFactory.iterator());
        } catch (Exception e) {
            log.error("Unable to process data from file {}", path, e);
        }
    }
}
