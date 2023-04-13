package org.emel.CurrencyConversionService.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Класс объекта передачи данных для модели Conversion
 */
public class ConversionDTO {
    private String fromCurrency;
    private String toCurrency;
    private double currencyRate;
    private long quantity;
    private BigDecimal totalResult;
    private String convertedAt;

    public ConversionDTO(){

    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(double currencyRate) {
        this.currencyRate = currencyRate;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(BigDecimal totalResult) {
        this.totalResult = totalResult;
    }

    public String getConvertedAt() {
        return convertedAt;
    }

    public void setConvertedAt(String convertedAt) {
        this.convertedAt = convertedAt;
    }
}
