package org.emel.ClientService.dto;

/**
 * Класс объекта передачи входных данных от клиента
 */
public class ConversionInputDataDTO {

    private String fromCurrency;

    private String toCurrency;

    private int quantity;

    public ConversionInputDataDTO() {

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
