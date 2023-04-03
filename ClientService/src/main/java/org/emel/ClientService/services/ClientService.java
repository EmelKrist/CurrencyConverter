package org.emel.ClientService.services;

import org.emel.ClientService.dto.ConversionInputDataDTO;
import org.emel.ClientService.dto.ConversionsResponse;
import org.emel.ClientService.models.Conversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Сервис для клиента
 */
@Service
public class ClientService {

    @Value("${microservice-currency-conversion.url}")
    private String url;

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
                throw new RuntimeException(responseEntity.getStatusCode().getReasonPhrase());
            }
            // если у сущности есть тело (json)
            if (responseEntity.hasBody()) {
                log.debug("Successful conversion response from the currency conversion service");
                return responseEntity.getBody();
            }
        } catch (Exception e) {
            /* TODO добавить поле для хранения ошибки (в контроллере проверять на null),
                в случае null Добавлять на представление метку с сообщением об ошибке */
            log.debug("Program error because of{}", e.getMessage());
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

    /**
     * Метод получения истории конвертаций
     *
     * @return список с конвертациями
     */
    public List<Conversion> getListOfConversions() {
        log.debug("Gets a list of conversions");
        try {
            final var responseEntity = sendGetHttpRequestToGetListOfConversions();

            if (responseEntity.getStatusCode().isError()) {
                throw new RuntimeException(responseEntity.getStatusCode().getReasonPhrase());
            }

            if (responseEntity.hasBody()) {
                log.debug("Successfully getting a list of conversions from the currency conversion service");
                return responseEntity.getBody().getConversions();
            }
        } catch (Exception e) {
            /* TODO добавить поле для хранения ошибки (в контроллере проверять на null),
                в случае null Добавлять на представление метку с сообщением об ошибке */
            log.debug("Program error because of{}", e.getMessage());
        }
        return null; // в противном случае возвращаем пустой список
    }

    /**
     * Метод для отправки GET HTTP запроса для получения списка конвертаций
     * от сервиса конвертации валют
     *
     * @return ответ от сервиса конвертации валют
     */
    private ResponseEntity<ConversionsResponse> sendGetHttpRequestToGetListOfConversions() {
        log.debug("Sends an HTTP GET request to the currency conversion service to get list of conversions");
        // отправляем запрос сервису конвертации валют для получения истории конвертаций
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url, ConversionsResponse.class);
    }
}
