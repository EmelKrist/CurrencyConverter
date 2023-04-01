package org.emel.CurrencyConversionService.repositories;

import org.emel.CurrencyConversionService.models.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для сущности Conversion (числовой id)
 */
@Repository
public interface ConversionsRepository extends JpaRepository<Conversion, Integer> {

    // все конвертации, отсортированные по убыванию времени создания
    List<Conversion> findAllByOrderByConvertedAtDesc();
}
