package com.gft.assignment.tradecapture.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;

@Value
@Builder
public class TradeRecordProcessed {
    String tradeReference;
    Long accountNumber;
    String stockCode;
    BigDecimal quantity;
    Currency currency;
    BigDecimal price;
    String broker;
    BigDecimal amount;
    OffsetDateTime receivedTimestamp;
}
