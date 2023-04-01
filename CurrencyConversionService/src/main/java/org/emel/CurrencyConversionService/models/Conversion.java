package org.emel.CurrencyConversionService.models;

import javax.persistence.*;

import java.time.LocalDateTime;

/**
 * Класс модели (сущности) Conversion
 */
@Entity
@Table(name = "Conversion")
public class Conversion {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "from_currency")
    private String fromCurrency;
    @Column(name = "to_currency")
    private String toCurrency;
    @Column(name = "currency_rate")
    private double currencyRate;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "total_result")
    private double totalResult;
    @Column(name = "converted_at")
    private LocalDateTime convertedAt;

    public Conversion() {
    }

    public Conversion(String fromCurrency, String toCurrency, double currencyRate, int quantity) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.currencyRate = currencyRate;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
