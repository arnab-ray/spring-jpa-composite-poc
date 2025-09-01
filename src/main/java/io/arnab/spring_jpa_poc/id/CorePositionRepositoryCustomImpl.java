package io.arnab.spring_jpa_poc.id;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@Component
//@Repository
public class CorePositionRepositoryCustomImpl implements CorePositionRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CorePosition2> findAllById(List<CorePositionId2> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CorePosition2> cq = cb.createQuery(CorePosition2.class);
        Root<CorePosition2> root = cq.from(CorePosition2.class);

        List<Predicate> predicates = new ArrayList<>();

        for (CorePositionId2 id : ids) {
            predicates.add(cb.and(
                    cb.equal(root.get("ucc"), id.getUcc()),
                    cb.equal(root.get("symbol"), id.getSymbol()),
                    cb.equal(root.get("product"), id.getProduct()),
                    cb.equal(root.get("exchange"), id.getExchange()),
                    cb.equal(root.get("tradingSessionId"), id.getTradingSessionId())
            ));
        }

        cq.where(cb.or(predicates.toArray(new Predicate[0])));

        TypedQuery<CorePosition2> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
