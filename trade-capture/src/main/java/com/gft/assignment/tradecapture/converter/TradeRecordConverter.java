package com.gft.assignment.tradecapture.converter;

import com.gft.assignment.tradecapture.model.TradeRecord;
import com.gft.assignment.tradecapture.model.TradeRecordProcessed;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        BigDecimal amount = source.getQuantity().setScale(2, RoundingMode.UP).multiply(source.getPrice());
        return TradeRecordProcessed.builder()
                .accountNumber(source.getAccountNumber())
                .broker(source.getBroker())
                .currency(source.getCurrency())
                .price(source.getPrice())
                .quantity(source.getQuantity())
                .stockCode(source.getStockCode())
                .tradeReference(source.getTradeReference())
                .amount(amount)
                .receivedTimestamp(timestamp)
                .build();
    }
}
