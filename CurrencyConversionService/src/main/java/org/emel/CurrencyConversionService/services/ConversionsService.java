package org.emel.CurrencyConversionService.services;

import org.emel.CurrencyConversionService.models.Conversion;
import org.emel.CurrencyConversionService.util.CurrencyRateIsNotSupported;
import org.emel.CurrencyConversionService.util.ExchangeRateRestApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Сервис для модели Conversion
 */
@Service
public class ConversionsService {
    private final Logger log = LoggerFactory.getLogger(ConversionsService.class);
    @Value("${exchange.rate.api.url}")
    private String url;

    /**
     * Метод конвертации
     *
     * @param conversion объект конвертации с исходными данными
     * @return объект конвертации с результатами конвертации (если они существуют)
     * @throws CurrencyRateIsNotSupported валютная ставка не поддерживается
     */
    public Conversion convert(Conversion conversion) throws CurrencyRateIsNotSupported {
        // получаем валютную ставку
        String rate = sendHttpRequestToGetCurrencyRate(conversion.getFromCurrency(), conversion.getToCurrency());
        if (rate != null) { // если она существует
            // конвертируем данные
            try {
                conversion.setCurrencyRate(Double.parseDouble(rate));
                conversion.setTotalResult(calcConversionResult(conversion.getCurrencyRate(), conversion.getQuantity()));
                conversion.setConvertedAt(String.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"))));
                return conversion; // конвертация с результатами
            } catch (NumberFormatException e) {
                log.debug("Failed to parse the exchange rate");
                throw new CurrencyRateIsNotSupported("Неподдерживаемый формат валютной ставки!");
            }
        } else {
            log.debug("Failed to get data from Coingate REST API");
            throw new CurrencyRateIsNotSupported("Выбранная валютная ставка не поддерживается!");
        }
    }

    /**
     * Метод отправки GET HTTP запроса для получения валютной ставки
     *
     * @param from валюта, из которой конвертируют
     * @param to   валюта, в которую конвертируют
     * @return строковая валютная ставка
     */
    private String sendHttpRequestToGetCurrencyRate(String from, String to) {
        try {
            log.debug("Sends a GET HTTP request to get exchange rate from {} to {} from Coingate REST API", from, to);
            return new RestTemplate().getForObject(String.format("%s/%s/%s", this.url, from, to), String.class);
        } catch (HttpServerErrorException | HttpClientErrorException | UnknownHttpStatusCodeException e) {
            throw new ExchangeRateRestApiException("Сервис получения валютной ставки недоступен!");
        }
    }

    /**
     * Метод для конвертации суммы по валютной ставке
     *
     * @param rate     валютная ставка
     * @param quantity сумма
     * @return эквивалент суммы в другой валюте
     */
    private BigDecimal calcConversionResult(double rate, long quantity) {
        log.debug("Converts quantity {} using the received rate {}", rate, quantity);
        BigDecimal result = new BigDecimal(rate).multiply(new BigDecimal(quantity));
        int scale = 2;
        if (result.compareTo(new BigDecimal(1)) <= 0) scale = 6;

        return result.setScale(scale, RoundingMode.FLOOR);
    }
}
