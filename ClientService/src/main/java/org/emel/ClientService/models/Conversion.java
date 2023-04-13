package org.emel.ClientService.models;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Класс модели Conversion
 */
public class Conversion {
    private String fromCurrency;
    private String toCurrency;
    private double currencyRate;
    @Min(value = 1, message = "Сумма конвертации должна быть больше 0!")
    private long quantity = 1;
    private BigDecimal totalResult;
    private String convertedAt = null;

    public Conversion(){

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
