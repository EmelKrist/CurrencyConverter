package org.emel.ClientService.dto;

import org.emel.ClientService.models.Conversion;

import java.util.List;

/**
 * Класс объекта передачи данных для списка сущностей Conversion
 */
public class ConversionsResponse {
    private List<Conversion> conversions;

    public ConversionsResponse() {
    }

    public List<Conversion> getConversions() {
        return conversions;
    }

    public void setConversions(List<Conversion> conversions) {
        this.conversions = conversions;
    }
}
