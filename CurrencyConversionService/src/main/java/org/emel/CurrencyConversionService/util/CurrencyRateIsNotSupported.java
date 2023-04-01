package org.emel.CurrencyConversionService.util;

/**
 * Пользовательское исключение "Валютная ставка не поддерживается"
 */
public class CurrencyRateIsNotSupported extends RuntimeException{
    public CurrencyRateIsNotSupported(String message){
        super(message);
    }
}
