package org.apache.fineract.cn.stellarbridge.service.internal.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StellarCursorRepository extends CrudRepository<StellarCursorEntity, Long> {
  Optional<StellarCursorEntity> findTopByProcessedTrueOrderByCreatedOnDesc();
  Optional<StellarCursorEntity> findByCursor(String cursor);
}
