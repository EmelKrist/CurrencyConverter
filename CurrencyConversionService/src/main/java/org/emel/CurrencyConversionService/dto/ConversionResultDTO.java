package org.emel.CurrencyConversionService.dto;

import java.time.LocalDateTime;

/**
 * Класс передачи данных с результатом конвертации клиенту
 */
public class ConversionResultDTO {
    private double currencyRate;
    private double totalResult;
    private LocalDateTime convertedAt;

    public ConversionResultDTO() {

    }

    public double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(double currencyRate) {
        this.currencyRate = currencyRate;
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
