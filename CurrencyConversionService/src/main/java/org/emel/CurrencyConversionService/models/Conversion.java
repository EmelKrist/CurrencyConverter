package org.emel.CurrencyConversionService.models;


import java.time.LocalDateTime;


/**
 * Класс модели Conversion
 */
public class Conversion {
    private String fromCurrency;
    private String toCurrency;
    private double currencyRate;
    private int quantity;
    private double totalResult;
    private LocalDateTime convertedAt;

    public Conversion() {
    }

    public Conversion(String fromCurrency, String toCurrency, double currencyRate, int quantity) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.currencyRate = currencyRate;
        this.quantity = quantity;
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
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(double totalResult) {
        this.totalResult = totalResult;
    }

    public LocalDateTime getConvertedAt() {
        return convertedAt;
    }

    public void setConvertedAt(LocalDateTime convertedAt) {
        this.convertedAt = convertedAt;
    }
}
