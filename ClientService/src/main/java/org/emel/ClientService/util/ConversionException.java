package org.emel.ClientService.util;

/**
 * Пользовательское исключение "Ошибка конвертации"
 */
public class ConversionException extends RuntimeException {
    public ConversionException(String message) {
        super(message);
    }
}
