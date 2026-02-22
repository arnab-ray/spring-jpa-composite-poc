package io.arnab.spring_jpa_poc.embedded;

import com.groww.stocks.order.sdk.common.enums.StocksProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CorePositionRepository extends JpaRepository<CorePosition, CorePositionId> {
    List<CorePosition> findByCorePositionIdIn(List<CorePositionId> corePositionIds);

    Optional<CorePosition> findByCorePositionIdUccAndCorePositionIdSymbolAndCorePositionIdProduct(String ucc, String symbol, StocksProduct product);
}
