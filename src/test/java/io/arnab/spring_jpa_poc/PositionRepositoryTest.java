package io.arnab.spring_jpa_poc;

import com.groww.stocks.order.sdk.common.enums.StockExchange;
import com.groww.stocks.order.sdk.common.enums.StocksProduct;
import io.arnab.spring_jpa_poc.embedded.Position;
import io.arnab.spring_jpa_poc.embedded.PositionId;
import io.arnab.spring_jpa_poc.embedded.PositionRepository;
import io.arnab.spring_jpa_poc.id.Position2;
import io.arnab.spring_jpa_poc.id.PositionId2;
import io.arnab.spring_jpa_poc.id.PositionRepository2;
import io.arnab.spring_jpa_poc.id.PositionRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext
public class PositionRepositoryTest {

    @Autowired private PositionRepository positionRepository;

    @Autowired private PositionRepositoryCustom positionRepositoryCustom;

    @Autowired private PositionRepository2 positionRepository2;

    @PersistenceContext private EntityManager entityManager;

    @Test
    void testFindInClause() {
        final List<Position> positions = List.of(
                Position.builder()
                        .positionId(new PositionId("test-ucc", "test-symbol-1", StocksProduct.CNC, StockExchange.NSE, 1))
                        .build(),
                Position.builder()
                        .positionId(new PositionId("test-ucc", "test-symbol-2", StocksProduct.CNC, StockExchange.NSE, 1))
                        .build(),
                Position.builder()
                        .positionId(new PositionId("test-ucc-2", "test-symbol-2", StocksProduct.CNC, StockExchange.BSE, 1))
                        .build()
        );


        for (Position position : positions) {
            entityManager.persist(position);
            entityManager.flush();
        }
        entityManager.clear(); // Clear the persistence context to avoid caching issues

        var response = positionRepository.findByPositionIdIn(
                List.of(
                        new PositionId("test-ucc", "test-symbol-1", StocksProduct.CNC, StockExchange.NSE, 1),
                        new PositionId("test-ucc-2", "test-symbol-2", StocksProduct.CNC, StockExchange.BSE, 1)
                )
        );

        assertThat(response).hasSize(2);
        assertThat(response).usingRecursiveComparison().isEqualTo(
                List.of(
                        positions.get(0),
                        positions.get(2)
                )
        );
    }

    @Test
    void testFindByPartial() {
        final List<Position> positions = List.of(
                Position.builder()
                        .positionId(new PositionId("test-ucc", "test-symbol-1", StocksProduct.CNC, StockExchange.NSE, 1))
                        .build(),
                Position.builder()
                        .positionId(new PositionId("test-ucc", "test-symbol-2", StocksProduct.CNC, StockExchange.NSE, 1))
                        .build(),
                Position.builder()
                        .positionId(new PositionId("test-ucc-2", "test-symbol-2", StocksProduct.CNC, StockExchange.BSE, 1))
                        .build()
        );


        for (Position position : positions) {
            entityManager.persist(position);
            entityManager.flush();
        }
        entityManager.clear(); // Clear the persistence context to avoid caching issues

        var response = positionRepository.findByPositionIdUccAndPositionIdSymbolAndPositionIdProduct("test-ucc", "test-symbol-1", StocksProduct.CNC);

        assertThat(response).isPresent();
        assertThat(response.get()).usingRecursiveComparison().isEqualTo(positions.getFirst());
    }

    @Test
    void testFindInCustomQuery() {
        final List<Position2> corePositions2 = List.of(
                Position2.builder()
                        .ucc("test-ucc").symbol("test-symbol-1").product(StocksProduct.CNC).exchange(StockExchange.NSE).tradingSessionId(1)
                        .build(),
                Position2.builder()
                        .ucc("test-ucc").symbol("test-symbol-2").product(StocksProduct.CNC).exchange(StockExchange.NSE).tradingSessionId(1)
                        .build(),
                Position2.builder()
                        .ucc("test-ucc-2").symbol("test-symbol-2").product(StocksProduct.CNC).exchange(StockExchange.BSE).tradingSessionId(1)
                        .build()
        );

        for (Position2 position2 : corePositions2) {
            entityManager.persist(position2);
            entityManager.flush();
        }
        entityManager.clear();

        var response =
                positionRepositoryCustom.findAllById(
                        List.of(
                                new PositionId2("test-ucc", "test-symbol-1", StocksProduct.CNC, StockExchange.NSE, 1),
                                new PositionId2("test-ucc-2", "test-symbol-2", StocksProduct.CNC, StockExchange.BSE, 1)));

        assertThat(response).hasSize(2);
        assertThat(response).usingRecursiveComparison().isEqualTo(
                List.of(
                        corePositions2.get(0),
                        corePositions2.get(2)
                )
        );
    }

    @Test
    void testFindAllById() {
        final List<Position2> corePositions2 = List.of(
                Position2.builder()
                        .ucc("test-ucc").symbol("test-symbol-1").product(StocksProduct.CNC).exchange(StockExchange.NSE).tradingSessionId(1)
                        .build(),
                Position2.builder()
                        .ucc("test-ucc").symbol("test-symbol-2").product(StocksProduct.CNC).exchange(StockExchange.NSE).tradingSessionId(1)
                        .build(),
                Position2.builder()
                        .ucc("test-ucc-2").symbol("test-symbol-2").product(StocksProduct.CNC).exchange(StockExchange.BSE).tradingSessionId(1)
                        .build()
        );

        for (Position2 position2 : corePositions2) {
            entityManager.persist(position2);
            entityManager.flush();
        }
        entityManager.clear();

        var response =
                positionRepository2.findAllById(
                        List.of(
                                new PositionId2("test-ucc", "test-symbol-1", StocksProduct.CNC, StockExchange.NSE, 1),
                                new PositionId2("test-ucc-2", "test-symbol-2", StocksProduct.CNC, StockExchange.BSE, 1)));

        assertThat(response).hasSize(2);
        assertThat(response).usingRecursiveComparison().isEqualTo(
                List.of(
                        corePositions2.get(0),
                        corePositions2.get(2)
                )
        );
    }
}
