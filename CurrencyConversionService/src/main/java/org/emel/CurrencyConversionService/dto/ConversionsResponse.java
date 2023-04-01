package org.emel.CurrencyConversionService.dto;

import java.util.List;

/**
 * Класс объекта передачи данных для списка сущностей Conversion
 */
public class ConversionsResponse {
    private List<ConversionDTO> conversions;

    public ConversionsResponse(List<ConversionDTO> conversions) {
        this.conversions = conversions;
    }

    public List<ConversionDTO> getConversions() {
        return conversions;
    }

    public void setConversions(List<ConversionDTO> conversions) {
        this.conversions = conversions;
    }
}
