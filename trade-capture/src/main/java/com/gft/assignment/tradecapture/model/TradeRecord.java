package com.gft.assignment.tradecapture.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
public class TradeRecord {
    private String tradeReference;
    private Long accountNumber;
    private String stockCode;
    private BigDecimal quantity;
    private Currency currency;
    private BigDecimal price;
    private String broker;
}
