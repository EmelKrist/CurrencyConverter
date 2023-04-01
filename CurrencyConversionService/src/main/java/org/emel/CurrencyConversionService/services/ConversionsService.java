package org.emel.CurrencyConversionService.services;

import org.emel.CurrencyConversionService.models.Conversion;
import org.emel.CurrencyConversionService.repositories.ConversionsRepository;
import org.emel.CurrencyConversionService.util.CurrencyRateIsNotSupported;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Сервис для сущности Conversion
 */
@Service
public class ConversionsService {

    private final Logger log = LoggerFactory.getLogger(ConversionsService.class);
    private final ConversionsRepository conversionsRepository;
    @Value("${exchange.rate.api.url}")
    private String url;

    @Autowired
    public ConversionsService(ConversionsRepository conversionsRepository) {
        this.conversionsRepository = conversionsRepository;
    }

    /**
     * Метод для сохранения успешной конвертации в БД
     *
     * @param conversion конвертация
     */
    @Transactional
    public void save(Conversion conversion) {
        log.debug("Saves to database conversion : {}", conversion);
        conversionsRepository.save(conversion);
    }

    /**
     * Метод конвертации
     *
     * @param conversion объект конвертации с исходными данными
     * @return объект конвертации с результатами конвертации (если они существуют)
     * @throws CurrencyRateIsNotSupported валютная ставка не поддерживается
     */
    public Optional<Conversion> convert(Conversion conversion) throws CurrencyRateIsNotSupported {
        // получаем валютную ставку
        String rate = sendHttpRequestToGetCurrencyRate(conversion.getFromCurrency(), conversion.getToCurrency());
        if (rate != null) { // если она существует
            // конвертируем данные
            conversion.setCurrencyRate(Double.parseDouble(rate));
            conversion.setTotalResult(calcConversionResult(conversion.getCurrencyRate(), conversion.getQuantity()));
            conversion.setConvertedAt(LocalDateTime.now());
            return Optional.of(conversion); // конвертация с результатами
        }
        // иначе конвертация невозможна
        return Optional.empty();
    }

    /**
     * Метод отправки GET HTTP запроса для получения валютной ставки
     *
     * @param from валюта, из которой конвертируют
     * @param to   валюта, в которую конвертируют
     * @return строковая валютная ставка
     */
    private String sendHttpRequestToGetCurrencyRate(String from, String to) {
        log.debug("Sends a GET HTTP request to get exchange rate from {} to {} from Coingate REST API", from, to);
        return new RestTemplate().getForObject(String.format("%s/%s/%s", this.url, from, to), String.class);
        //TODO: сделать обработку ошибок в случае отказа Coingate REST API
    }

    /**
     * Метод для конвертации суммы по валютной ставке
     *
     * @param rate     валютная ставка
     * @param quantity сумма
     * @return эквивалент суммы в другой валюте
     */
    private double calcConversionResult(double rate, int quantity) {
        log.debug("Converts quantity {} using the received rate {}", rate, quantity);
        return rate * quantity;
    }

}
