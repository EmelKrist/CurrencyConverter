package org.emel.ClientService.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Класс модели Conversion
 */
@NoArgsConstructor
@Getter
@Setter
public class Conversion {
    private String fromCurrency;
    private String toCurrency;
    private double currencyRate;
    @Min(value = 1, message = "Сумма конвертации должна быть больше 0!")
    private long quantity = 1;
    private BigDecimal totalResult;
    private String convertedAt = null;
    private String error = null;

    /**
     * Метод для проверки конвертации на успешность
     * @return есть ошибки или нет (boolean)
     */
    public boolean isSuccessful() {
        return error == null;
    }
}
