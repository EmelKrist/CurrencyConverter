package org.emel.ClientService.services;

import org.emel.ClientService.models.Currency;
import org.emel.ClientService.repositories.CurrenciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * Метод для получения списка всех доступных клиенту валют из БД
     * Список сортируется по имени валюты (в алфавитном порядке)
     * @return список доступных валют
     */
    @Transactional(readOnly = true)
    public List<Currency> findAll() {
        return currenciesRepository.findAll(Sort.by("name"));
    }
}
