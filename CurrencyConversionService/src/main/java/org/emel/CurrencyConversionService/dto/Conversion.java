package org.emel.CurrencyConversionService.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Класс модели Conversion
 */
@Getter
@Setter
@NoArgsConstructor
public class Conversion {
    private String fromCurrency;
    private String toCurrency;
    private double currencyRate;
    private long quantity;
    private BigDecimal totalResult;
    private String convertedAt;
    private String error;
}
