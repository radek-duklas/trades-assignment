package com.gft.assignment.tradecapture.converter;

import com.gft.assignment.tradecapture.model.TradeRecord;
import com.gft.assignment.tradecapture.model.TradeRecordProcessed;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class TradeRecordConverter implements Converter<TradeRecord, TradeRecordProcessed> {
    private OffsetDateTime timestamp;

    @Override
    public void init() {
        timestamp = OffsetDateTime.now();
    }

    @Override
    public TradeRecordProcessed convert(TradeRecord source) {
        if (source.getQuantity() == null || source.getPrice() == null) {
            throw new IllegalArgumentException("Invalid data - both quantity and price must not be null");
        }
        return TradeRecordProcessed.builder()
                .accountNumber(source.getAccountNumber())
                .broker(source.getBroker())
                .currency(source.getCurrency())
                .price(source.getPrice())
                .quantity(source.getQuantity())
                .stockCode(source.getStockCode())
                .tradeReference(source.getTradeReference())
                //TODO rounding, error handling
                .amount(source.getQuantity().multiply(source.getPrice()))
                .receivedTimestamp(timestamp)
                .build();
    }
}
