package io.arnab.spring_jpa_poc.id;

import java.util.List;

public interface CorePositionRepositoryCustom {
    List<CorePosition2> findAllById(List<CorePositionId2> ids);
}
