package io.arnab.spring_jpa_poc.id;

import com.groww.stocks.order.sdk.common.enums.StockExchange;
import com.groww.stocks.order.sdk.common.enums.StocksProduct;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CorePositionId2 implements Serializable {
    private String ucc;
    private String symbol;
    private StocksProduct product;
    @Enumerated(EnumType.STRING)
    private StockExchange exchange;
    private Integer tradingSessionId;
}
