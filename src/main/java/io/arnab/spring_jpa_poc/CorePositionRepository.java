package io.arnab.spring_jpa_poc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorePositionRepository extends JpaRepository<CorePosition, CorePositionId> {
    List<CorePosition> findByCorePositionIdIn(List<CorePositionId> corePositionIds);
}
