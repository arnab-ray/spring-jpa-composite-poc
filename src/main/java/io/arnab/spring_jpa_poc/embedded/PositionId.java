package io.arnab.spring_jpa_poc.embedded;

import com.groww.stocks.order.sdk.common.enums.StockExchange;
import com.groww.stocks.order.sdk.common.enums.StocksProduct;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serializable;

@Embeddable
public record PositionId(
        String ucc,
        String symbol,
        @Enumerated(EnumType.STRING)
        StocksProduct product,
        @Enumerated(EnumType.STRING)
        StockExchange exchange,
        Integer tradingSessionId) implements Serializable {}
