package org.emel.ClientService.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.emel.ClientService.dto.ConversionInputDataDTO;
import org.emel.ClientService.models.Conversion;
import org.emel.ClientService.util.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис для клиента
 */
@Service
public class ClientService {
    private String serviceError;
    private final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ClientService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Метод конвертации валют для исходных данных из формы
     *
     * @param conversionInputDataDTO исходные данные, посылаемые сервису конвертации валют
     * @return объект конвертации с результатом от сервиса конвертации валют
     */
    public Conversion getConvert(ConversionInputDataDTO conversionInputDataDTO)  {
        log.debug("Converts for input: {}", conversionInputDataDTO);
        try {
            String response = (String) rabbitTemplate.convertSendAndReceive(
                    "ccExchange",
                    "ccRoutingKey",
                    new ObjectMapper().writeValueAsString(conversionInputDataDTO));
            Conversion conversionWithResult = new ObjectMapper().readValue(response, Conversion.class);
            // если есть ошибка конвертации, то выбрасываем исключение ConversionException
            if (conversionWithResult.getError() != null) {
                throw new ConversionException(conversionWithResult.getError());
            }
            return conversionWithResult;
        } catch (ConversionException e){
            log.debug("Program error because of {}", e.getMessage());
            serviceError = e.getMessage();
        } catch (Exception e) {
            log.debug("Program error because of {}", e.getMessage());
            serviceError = "Сервис конвертации валют недоступен!";
        }
        return null;
    }

    public String getServiceError() {
        return serviceError;
    }

}
