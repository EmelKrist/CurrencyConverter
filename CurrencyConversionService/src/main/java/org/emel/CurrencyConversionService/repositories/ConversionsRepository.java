package org.emel.CurrencyConversionService.repositories;

import org.emel.CurrencyConversionService.models.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для сущности Conversion (числовой id)
 */
@Repository
public interface ConversionsRepository extends JpaRepository<Conversion, Integer> {
}
