package org.emel.CurrencyConversionService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.emel.CurrencyConversionService.dto.Conversion;
import org.emel.CurrencyConversionService.dto.ConversionInputDataDTO;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static org.emel.CurrencyConversionService.config.RabbitConfiguration.QUEUE;

/**
 * Сервис для обработки сообщений в очереди брокера
 */
@Service
public class ProcessingQueueService {
    public final ConversionsService conversionsService;
    private final ModelMapper modelMapper;

    public ProcessingQueueService(ConversionsService conversionsService, ModelMapper modelMapper) {
        this.conversionsService = conversionsService;
        this.modelMapper = modelMapper;
    }

    /**
     * Слушатель для RabbitMQ очереди
     *
     * @param message сообщение, полученное из очереди
     * @return json с результатом конвертации
     * @throws JsonProcessingException ошибка обработки json
     */
    @RabbitListener(queues = QUEUE)
    public String process(String message) throws JsonProcessingException {
        ConversionInputDataDTO conversionInputDataDTO = new ObjectMapper().readValue(message, ConversionInputDataDTO.class);
        Conversion conversion = conversionsService.convert(convertToConversion(conversionInputDataDTO));
        return new ObjectMapper().writeValueAsString(conversion);
    }

    /**
     * Метод конвертации объекта ConversionInputDataDTO в объект модели Conversion
     *
     * @param conversionInputDataDTO объект передачи данных от клиента
     * @return объект модели Conversion
     */
    private Conversion convertToConversion(ConversionInputDataDTO conversionInputDataDTO) {
        return modelMapper.map(conversionInputDataDTO, Conversion.class);
    }
}
