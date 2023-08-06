package org.emel.CurrencyConversionService.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс объекта передачи входных данных от клиента
 */
@Getter
@Setter
@NoArgsConstructor
public class ConversionInputDataDTO {
    private String fromCurrency;
    private String toCurrency;
    private long quantity;
}
