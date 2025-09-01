package io.arnab.spring_jpa_poc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.groww.stocks.order.sdk.common.enums.StockExchange;
import com.groww.stocks.order.sdk.common.enums.StockSegment;
import com.groww.stocks.order.sdk.common.enums.StocksProduct;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "core_position",
        indexes = {
                @Index(name = "idx_tr_positions_ucc_symbol_product_exchange_trading_session_id",
                        columnList = "ucc, symbol, product, exchange, tradingSessionId", unique = true
                )}
)
@Entity
@DynamicUpdate
//@IdClass(CorePositionId.class)
@ToString
public class CorePosition extends BaseEntity {
    @EmbeddedId
    private CorePositionId corePositionId;

//    @Id
//    private String ucc;
//    @Id
//    private String symbol;
    private String tradeSymbol;
    @Enumerated(EnumType.STRING)
    private StockSegment segment;
//    @Id
//    @Enumerated(EnumType.STRING)
//    private StocksProduct product;
//    @Id
//    @Enumerated(EnumType.STRING)
//    private StockExchange exchange;
//    @Id
//    private Integer tradingSessionId;
    private double creditQty;
    private double creditPrice;
    private double debitQty;
    private double debitPrice;
    private double cfCreditQty;
    private double cfCreditPrice;
    private double cfDebitQty;
    private double cfDebitPrice;
    private double closedQty;
    private double closedCreditPrice;
    private double closedDebitPrice;
    @JsonIgnore
    private String trSystem;
    private long trTimeStamp;
    @JsonIgnore
    private boolean archivalFlag;
//    @JsonIgnore
//    public String getKey() {
//        return ucc.concat(symbol).concat(product.name()).concat(exchange.name()).concat(tradingSessionId.toString());
//    }

    @JsonIgnore
    public String getKey() {
        return corePositionId.getUcc().concat(corePositionId.getSymbol()).concat(corePositionId.getProduct().name())
                .concat(corePositionId.getExchange().name()).concat(corePositionId.getTradingSessionId().toString());
    }

//    @JsonIgnore
//    public CorePositionId getId() {
//        return CorePositionId.builder()
//                .ucc(ucc)
//                .symbol(symbol)
//                .product(product)
//                .exchange(exchange)
//                .tradingSessionId(tradingSessionId)
//                .build();
//    }

    @JsonIgnore
    public CorePositionId getId() {
        return corePositionId;
    }
}
