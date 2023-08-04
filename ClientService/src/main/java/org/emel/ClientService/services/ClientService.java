package org.emel.ClientService.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.emel.ClientService.dto.ConversionInputDataDTO;
import org.emel.ClientService.models.Conversion;
import org.emel.ClientService.util.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для клиента
 */
@Service
public class ClientService {
    private final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange-name}")
    private String exchange;

    @Value("${spring.rabbitmq.routing-key-name}")
    private String routingKey;
    private String serviceError;

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
    public Optional<Conversion> execConversion(ConversionInputDataDTO conversionInputDataDTO) {
        log.info("Converts for input: {}", conversionInputDataDTO);
        try {
            // делаем запрос к сервису конвертации и получаем ответ через брокер
            String response = (String) rabbitTemplate.convertSendAndReceive(
                    exchange, routingKey,
                    new ObjectMapper().writeValueAsString(conversionInputDataDTO));
            // получаем объект конвертации с результатом
            Conversion conversion = new ObjectMapper().readValue(response, Conversion.class);
            // если конвертация не успешна, то выбрасываем исключение ConversionException
            if (!conversion.isSuccessful()) {
                throw new ConversionException(conversion.getError());
            }
            return Optional.of(conversion);
        } catch (ConversionException e) {
            log.error("Program error because of {}", e.getMessage());
            serviceError = e.getMessage();
        } catch (Exception e) {
            log.error("Program error because of {}", e.getMessage());
            serviceError = "Сервис конвертации валют недоступен!";
        }
        return Optional.empty();
    }

    public String getServiceError() {
        return serviceError;
    }

}
