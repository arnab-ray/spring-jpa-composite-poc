package io.arnab.spring_jpa_poc;

import com.groww.stocks.order.sdk.common.enums.StockExchange;
import com.groww.stocks.order.sdk.common.enums.StocksProduct;
import io.arnab.spring_jpa_poc.embedded.CorePosition;
import io.arnab.spring_jpa_poc.embedded.CorePositionId;
import io.arnab.spring_jpa_poc.embedded.CorePositionRepository;
import io.arnab.spring_jpa_poc.id.CorePosition2;
import io.arnab.spring_jpa_poc.id.CorePositionId2;
import io.arnab.spring_jpa_poc.id.CorePositionRepositoryCustom;
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
public class CorePositionRepositoryTest {

    @Autowired private CorePositionRepository corePositionRepository;

    @Autowired private CorePositionRepositoryCustom corePositionRepositoryCustom;

    @PersistenceContext private EntityManager entityManager;

    @Test
    void testFindInClause() {
        final List<CorePosition> corePositions = List.of(
                CorePosition.builder()
                        .corePositionId(new CorePositionId("test-ucc", "test-symbol-1", StocksProduct.CNC, StockExchange.NSE, 1))
                        .build(),
                CorePosition.builder()
                        .corePositionId(new CorePositionId("test-ucc", "test-symbol-2", StocksProduct.CNC, StockExchange.NSE, 1))
                        .build(),
                CorePosition.builder()
                        .corePositionId(new CorePositionId("test-ucc-2", "test-symbol-2", StocksProduct.CNC, StockExchange.BSE, 1))
                        .build()
        );


        for (CorePosition corePosition : corePositions) {
            entityManager.persist(corePosition);
            entityManager.flush();
        }
        entityManager.clear(); // Clear the persistence context to avoid caching issues

        var response = corePositionRepository.findByCorePositionIdIn(
                List.of(
                        new CorePositionId("test-ucc", "test-symbol-1", StocksProduct.CNC, StockExchange.NSE, 1),
                        new CorePositionId("test-ucc-2", "test-symbol-2", StocksProduct.CNC, StockExchange.BSE, 1)
                )
        );

        assertThat(response).hasSize(2);
        assertThat(response).usingRecursiveComparison().isEqualTo(
                List.of(
                        corePositions.get(0),
                        corePositions.get(2)
                )
        );
    }

    @Test
    void testFindInCustomQuery() {
        final List<CorePosition2> corePositions2 = List.of(
                CorePosition2.builder()
                        .ucc("test-ucc").symbol("test-symbol-1").product(StocksProduct.CNC).exchange(StockExchange.NSE).tradingSessionId(1)
                        .build(),
                CorePosition2.builder()
                        .ucc("test-ucc").symbol("test-symbol-2").product(StocksProduct.CNC).exchange(StockExchange.NSE).tradingSessionId(1)
                        .build(),
                CorePosition2.builder()
                        .ucc("test-ucc-2").symbol("test-symbol-2").product(StocksProduct.CNC).exchange(StockExchange.BSE).tradingSessionId(1)
                        .build()
        );

        for (CorePosition2 corePosition2 : corePositions2) {
            entityManager.persist(corePosition2);
            entityManager.flush();
        }
        entityManager.clear();

        var response =
                corePositionRepositoryCustom.findAllById(
                        List.of(
                                new CorePositionId2("test-ucc", "test-symbol-1", StocksProduct.CNC, StockExchange.NSE, 1),
                                new CorePositionId2("test-ucc-2", "test-symbol-2", StocksProduct.CNC, StockExchange.BSE, 1)));

        assertThat(response).hasSize(2);
        assertThat(response).usingRecursiveComparison().isEqualTo(
                List.of(
                        corePositions2.get(0),
                        corePositions2.get(2)
                )
        );
    }
}
