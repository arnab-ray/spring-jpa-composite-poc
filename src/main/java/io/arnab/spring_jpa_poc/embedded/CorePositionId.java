package io.arnab.spring_jpa_poc.embedded;

import com.groww.stocks.order.sdk.common.enums.StockExchange;
import com.groww.stocks.order.sdk.common.enums.StocksProduct;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Embeddable
public class CorePositionId implements Serializable {
    private String ucc;
    private String symbol;
    @Enumerated(EnumType.STRING)
    private StocksProduct product;
    @Enumerated(EnumType.STRING)
    private StockExchange exchange;
    private Integer tradingSessionId;
}
