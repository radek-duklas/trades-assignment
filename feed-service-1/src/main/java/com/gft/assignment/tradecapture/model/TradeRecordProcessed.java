package com.gft.assignment.tradecapture.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;

@Data
@NoArgsConstructor
public class TradeRecordProcessed {
    private String tradeReference;
    private Long accountNumber;
    private String stockCode;
    private BigDecimal quantity;
    private Currency currency;
    private BigDecimal price;
    private String broker;
    private BigDecimal amount;
    private OffsetDateTime receivedTimestamp;
}
