package org.emel.ClientService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.emel.ClientService.dto.ConversionInputDataDTO;
import org.emel.ClientService.models.Conversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Сервис для клиента
 */
@Service
public class ClientService {
    @Value("${microservice-currency-conversion.url}")
    private String url;
    private String serverError;
    private final Logger log = LoggerFactory.getLogger(ClientService.class);

    /**
     * Метод конвертации валют по исходных данных из формы
     *
     * @param conversionInputDataDTO исходные данные, посылаемые сервису конвертации валют
     * @return объект конвертации с результатом от сервиса конвертации валют
     */
    public Conversion getConvert(ConversionInputDataDTO conversionInputDataDTO) {
        log.debug("Converts for input: {}", conversionInputDataDTO);
        try {
            final var responseEntity = sendPostHttpRequestForConversion(conversionInputDataDTO);
            // если в сущности обнаружена ошибка, то выбрасываем исключение
            if (responseEntity.getStatusCode().isError()) {
                throw new RuntimeException(String.valueOf(responseEntity.getBody()));
            }
            // если у сущности есть тело (json)
            if (responseEntity.hasBody()) {
                log.debug("Successful conversion response from the currency conversion service");
                return responseEntity.getBody();
            }
        } catch (HttpServerErrorException e) {
            log.debug("Program error because of{}", e.getMessage());
            serverError = "Сервис конвертации валют недоступен!";
        } catch (Exception e) {
            String message = e.getMessage();
            log.debug("Program error because of{}", message);
            try {
                JsonNode obj = new ObjectMapper().readTree(message.substring(7, message.length() - 1));
                serverError = obj.get("message").asText();
            } catch (JsonProcessingException | NullPointerException ex) {
                serverError = "Сервис конвертации валют недоступен!";
            }
        }
        return null;
    }

    /**
     * Метод отправки POST HTTP Запроса для конвертации на стороне сервиса конвертации валют
     *
     * @param conversionInputDataDTO входные данные для конвертации
     * @return ответ от сервиса конвертации валют
     */
    private ResponseEntity<Conversion> sendPostHttpRequestForConversion(ConversionInputDataDTO conversionInputDataDTO) {
        log.debug("Sends an HTTP POST request to the currency conversion service for conversion");
        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // отправляем запрос сервису конвертации валют и получаем ответ в виде сущности ответа
        final var request = new HttpEntity<>(conversionInputDataDTO, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(url, request, Conversion.class);
    }

    public String getServerError() {
        return serverError;
    }

    public void setServerError(String serverError) {
        this.serverError = serverError;
    }
}
