package io.arnab.spring_jpa_poc;

import com.groww.stocks.order.sdk.common.enums.StockExchange;
import com.groww.stocks.order.sdk.common.enums.StocksProduct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CorePositionRepositoryTest {

    @Autowired private CorePositionRepository corePositionRepository;

    @PersistenceContext private EntityManager entityManager;

    private final List<CorePosition> corePositions = List.of(
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

    @BeforeEach
    void setUp() {
        corePositionRepository.saveAll(corePositions);
        corePositionRepository.flush();
        entityManager.clear(); // Clear the persistence context to avoid caching issues
    }

    @Test
    void testFindInClause() {
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
}
