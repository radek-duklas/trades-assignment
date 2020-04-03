package com.gft.assignment.feedservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.assignment.tradecapture.model.TradeRecordProcessed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Slf4j
@Component
public class TradeRecordsConsumer implements CollectionConsumer<TradeRecordProcessed> {
    private ObjectMapper objectMapper;

    private Path location;

    private String filename;

    public TradeRecordsConsumer(ObjectMapper objectMapper,
                                @Value("${app.trade-records.target-location}") Path location,
                                @Value("${app.trade-records.target-filename}") String filename) {
        this.objectMapper = objectMapper;
        this.location = location;
        this.filename = filename;
    }

    @Override
    public void accept(Collection<TradeRecordProcessed> tradeRecords) {
        String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd_HHmmssSSS"));
        Path targetPath = location.resolve(filename + "_" + currentTimestamp);
        try (var stream = Files.newOutputStream(targetPath, StandardOpenOption.CREATE)) {
            objectMapper.writeValue(stream, tradeRecords);
            log.info("Saved {} records into {}", tradeRecords.size(), targetPath);
        } catch (IOException e) {
            log.error("Unable to save received data into target file {}", targetPath, e);
        }

    }
}
