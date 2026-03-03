package io.arnab.spring_jpa_poc.id;

import java.util.List;

public interface PositionRepositoryCustom {
    List<Position2> findAllById(List<PositionId2> ids);
}
