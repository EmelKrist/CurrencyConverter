package org.emel.ClientService.repositories;

import org.emel.ClientService.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для сущности Currency (строковый id)
 */
@Repository
public interface CurrenciesRepository extends JpaRepository<Currency, Integer> {
}
