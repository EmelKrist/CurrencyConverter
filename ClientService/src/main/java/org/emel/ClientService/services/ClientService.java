package org.emel.ClientService.services;

import org.emel.ClientService.dto.ConversionInputDataDTO;
import org.emel.ClientService.dto.ConversionsResponse;
import org.emel.ClientService.models.Conversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Сервис для клиента
 */
@Service
public class ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientService.class);

    /**
     * Метод конвертации валют по исходных данных из формы
     *
     * @param conversionInputDataDTO исходные данные, посылаемые сервису конвертации валют
     * @return объект конвертации с результатом от сервиса конвертации валют
     */
    public Conversion getConvert(ConversionInputDataDTO conversionInputDataDTO) {
        //TODO вынести urk и порт во внешний файл
        final var url = "http://localhost:8080/conversions";
        final var headers = new HttpHeaders(); // header запроса (указываем, что формат Запроса - json данные)
        headers.setContentType(MediaType.APPLICATION_JSON);

        try { // TODO отделить отправку запроса и обработку сущности ответа от сервиса конвертации валют
            // отправляем запрос сервису конвертации валют и получаем ответ в виде сущности ответа
            final var request = new HttpEntity<>(conversionInputDataDTO, headers);
            RestTemplate restTemplate = new RestTemplate();
            final var responseEntity = restTemplate.postForEntity(url, request, Conversion.class);
            //TODO проработать нормальную обработку ошибок и вывод их в логи, а также в веб. интерфейс

            // если в сущности обнаружена ошибка, то выбрасываем исключение
            if (responseEntity.getStatusCode().isError()) {
                throw new RuntimeException(responseEntity.getStatusCode().getReasonPhrase());
            }
            // если у сущности есть тело (json), возвращаем (Jackson сконвертирует в объект)
            if (responseEntity.hasBody()) {
                return responseEntity.getBody();
            }
        } catch (Exception e) {
            // если выброшено исключение, выводим сообщение
            System.out.println("Ошибка " + e.getMessage());
        }
        // если у ответа нет тела, возвращаем null
        return null;
    }

    /**
     * Метод получения истории конвертаций
     *
     * @return список с конвертациями
     */
    public List<Conversion> getListOfConversions() {
        // TODO вынести во внешний файл url
        final var url = "http://localhost:8080/conversions";

        try { // TODO отделить отправку запроса и обработку сущности ответа от сервиса конвертации валют
            // отправляем запрос сервису конвертации валют для получения истории конвертаций
            RestTemplate restTemplate = new RestTemplate();
            final var responseEntity = restTemplate.getForEntity(url, ConversionsResponse.class);
            //TODO проработать нормальную обработку ошибок и вывод их в логи, а также в веб. интерфейс

            // если в вернувшемся ответе ошибка, то выбрасываем исключение
            if (responseEntity.getStatusCode().isError()) {
                throw new RuntimeException(responseEntity.getStatusCode().getReasonPhrase());
            }
            // если ошибки нет и есть тело у сущности ответа, возвращаем его, получая список
            if (responseEntity.hasBody()) {
                return responseEntity.getBody().getConversions();
            }
        } catch (Exception e) {
            // если выброшено исключение, выводим сообщение
            System.out.println("Ошибка " + e.getMessage());
        }
        return Collections.emptyList(); // в противном случае возвращаем пустой список
    }
}
