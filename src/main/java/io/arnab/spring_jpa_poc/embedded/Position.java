package io.arnab.spring_jpa_poc.embedded;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.groww.stocks.order.sdk.common.enums.StockSegment;
import io.arnab.spring_jpa_poc.BaseEntity;
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
@ToString
public class Position extends BaseEntity {
    @EmbeddedId
    private PositionId positionId;
    private String tradeSymbol;
    @Enumerated(EnumType.STRING)
    private StockSegment segment;
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

    @JsonIgnore
    public String getKey() {
        return positionId.ucc().concat(positionId.symbol()).concat(positionId.product().name())
                .concat(positionId.exchange().name()).concat(positionId.tradingSessionId().toString());
    }

    @JsonIgnore
    public PositionId getId() {
        return positionId;
    }
}
