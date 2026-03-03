package io.arnab.spring_jpa_poc.id;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@Component
//@Repository
public class PositionRepositoryCustomImpl implements PositionRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Position2> findAllById(List<PositionId2> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Position2> cq = cb.createQuery(Position2.class);
        Root<Position2> root = cq.from(Position2.class);

        List<Predicate> predicates = new ArrayList<>();

        for (PositionId2 id : ids) {
            predicates.add(cb.and(
                    cb.equal(root.get("ucc"), id.getUcc()),
                    cb.equal(root.get("symbol"), id.getSymbol()),
                    cb.equal(root.get("product"), id.getProduct()),
                    cb.equal(root.get("exchange"), id.getExchange()),
                    cb.equal(root.get("tradingSessionId"), id.getTradingSessionId())
            ));
        }

        cq.where(cb.or(predicates.toArray(new Predicate[0])));

        TypedQuery<Position2> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
