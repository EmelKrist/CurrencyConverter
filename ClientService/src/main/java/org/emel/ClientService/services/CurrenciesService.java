package org.emel.ClientService.services;

import org.emel.ClientService.repositories.CurrenciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис для сущности Currency
 */
@Service
public class CurrenciesService {
    private final CurrenciesRepository currenciesRepository;

    @Autowired
    public CurrenciesService(CurrenciesRepository currenciesRepository) {
        this.currenciesRepository = currenciesRepository;
    }
}
