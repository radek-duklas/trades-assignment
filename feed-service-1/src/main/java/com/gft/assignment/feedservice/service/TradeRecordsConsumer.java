package com.gft.assignment.feedservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.assignment.feedservice.model.TradeRecordProcessed;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class TradeRecordsConsumer implements CollectionConsumer<TradeRecordProcessed> {
    private ObjectMapper objectMapper;

    @Override
    public void accept(Collection<TradeRecordProcessed> tradeRecords) {
        objectMapper.writeValue(tradeRecords);
    }
}
