package org.emel.CurrencyConversionService.util;

import java.time.LocalDateTime;

/**
 * Класс ответа с ошибкой
 */
public class ErrorResponse {
    private LocalDateTime timestamp; // время, когда произошла ошибка (м.сек)
    private int status; // статус ошибки(код)
    private String error; // расшифровка статуса
    private String path; // путь, по которому произошла ошибка
    private String message; // сообщение в ошибке

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String path, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
