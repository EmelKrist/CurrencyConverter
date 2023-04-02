package org.emel.ClientService.services;

import org.emel.ClientService.dto.ConversionInputDataDTO;
import org.emel.ClientService.models.Conversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
}
