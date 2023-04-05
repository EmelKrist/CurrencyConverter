package org.emel.CurrencyConversionService.util;

/**
 * Пользовательское исключение "Ошибка REST API курса валют"
 */
public class ExchangeRateRestApiException extends RuntimeException{
    public ExchangeRateRestApiException(String message){
        super(message);
    }
}
