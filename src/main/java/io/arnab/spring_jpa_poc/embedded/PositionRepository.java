package io.arnab.spring_jpa_poc.embedded;

import com.groww.stocks.order.sdk.common.enums.StocksProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, PositionId> {
    List<Position> findByPositionIdIn(List<PositionId> positionIds);

    Optional<Position> findByPositionIdUccAndPositionIdSymbolAndPositionIdProduct(String ucc, String symbol, StocksProduct product);
}
