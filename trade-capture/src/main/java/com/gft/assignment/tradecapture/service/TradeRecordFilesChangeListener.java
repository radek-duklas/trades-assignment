package com.gft.assignment.tradecapture.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gft.assignment.tradecapture.model.TradeRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Slf4j
@Component
@AllArgsConstructor
public class TradeRecordFilesChangeListener implements FileChangeListener {

    private ObjectMapper objectMapper;

    private DataCollectionConsumer<TradeRecord> consumer;

    @Override
    public void fileCreated(Path path) {
        ObjectReader reader = objectMapper.readerFor(TradeRecord.class);
        try {
            MappingIterator<TradeRecord> objectMappingIterator = reader.readValues(path.toFile());
            consumer.consumeNewData(objectMappingIterator);
        } catch (Exception e) {
            log.error("Unable to process data from file {}", path, e);
        }
    }

    @Override
    public void fileModified(Path path) {
        ObjectReader reader = objectMapper.readerFor(TradeRecord.class);
        try {
            MappingIterator<TradeRecord> objectMappingIterator = reader.readValues(path.toFile());
            consumer.consumeReplacingData(objectMappingIterator);
        } catch (Exception e) {
            log.error("Unable to process data from file {}", path, e);
        }
    }
}
