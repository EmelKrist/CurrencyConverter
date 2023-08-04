package org.emel.CurrencyConversionService.services;

import org.emel.CurrencyConversionService.dto.Conversion;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/**
 * Сервис для модели Conversion
 */
@Service
public class ConversionsService {
    private final Logger log = LoggerFactory.getLogger(ConversionsService.class);

    @Value("${exchange.rate.api.url}")
    private String apiUrl;

    /**
     * Метод конвертации
     *
     * @return объект конвертации с результатами конвертации (если они существуют)
     */
    public Conversion convert(Conversion conversion) {
        try { // получаем валютную ставку и, если она существует, конвертируем данные
            String rate = sendHttpRequestToGetCurrencyRate(conversion.getFromCurrency(), conversion.getToCurrency());
            if (rate != null) {
                conversion.setCurrencyRate(Double.parseDouble(rate));
                conversion.setTotalResult(calcConversionResult(conversion.getCurrencyRate(), conversion.getQuantity()));
                conversion.setConvertedAt(LocalDateTime.now(ZoneId.of("Europe/Moscow"))
                        .format(DateTimeFormatter.ofPattern("HH:mm")) + " MSK");
            } else {
                log.error("Failed to get data from Coingate REST API");
                conversion.setError("Выбранная валютная ставка не поддерживается!");
            }
        } catch (NumberFormatException e) {
            log.error("Failed to parse the exchange rate");
            conversion.setError("Выбранная валютная ставка не поддерживается!");
        } catch (Exception e) {
            log.error("The service for receiving a currency rate is unavailable!");
            conversion.setError("Сервис получения валютной ставки недоступен!");
        }
        return conversion; // конвертация с результатами
    }

    /**
     * Метод отправки GET HTTP запроса для получения валютной ставки
     *
     * @param from валюта, из которой конвертируют
     * @param to   валюта, в которую конвертируют
     * @return строковая валютная ставка
     */
    private String sendHttpRequestToGetCurrencyRate(String from, String to) throws
            HttpServerErrorException,
            HttpClientErrorException,
            UnknownHttpStatusCodeException {
        log.info("Sends a GET HTTP request to get exchange rate from {} to {} from Coingate REST API", from, to);
        return new RestTemplate().getForObject(String.format("%s/%s/%s", apiUrl, from, to), String.class);
    }

    /**
     * Метод для конвертации суммы по валютной ставке
     *
     * @param rate     валютная ставка
     * @param quantity сумма
     * @return эквивалент суммы в другой валюте
     */
    private BigDecimal calcConversionResult(double rate, long quantity) {
        log.info("Converts quantity {} using the received rate {}", rate, quantity);
        // умножаем сумму на валютную ставку
        BigDecimal result = new BigDecimal(rate).multiply(new BigDecimal(quantity));
        // если результат очень маленький, то кол-во знаков после запятой - 6, иначе - 2
        int scale = 2;
        if (result.compareTo(new BigDecimal(1)) <= 0) scale = 6;
        // округляем
        return result.setScale(scale, RoundingMode.FLOOR);
    }
}
