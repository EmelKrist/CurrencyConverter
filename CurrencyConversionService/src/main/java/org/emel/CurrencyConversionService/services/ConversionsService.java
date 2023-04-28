package org.emel.CurrencyConversionService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.emel.CurrencyConversionService.dto.ConversionInputDataDTO;
import org.emel.CurrencyConversionService.models.Conversion;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Сервис для модели Conversion
 */
@Service
public class ConversionsService {
    private final Logger log = LoggerFactory.getLogger(ConversionsService.class);
    @Value("${exchange.rate.api.url}")
    private String url;
    private final ModelMapper modelMapper;

    @Autowired
    public ConversionsService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Слушатель для RabbitMQ очереди ccQueue
     * @param message сообщение, полученное из очереди
     * @return json с результатом конвертации
     * @throws JsonProcessingException ошибка обработки json
     */
    @RabbitListener(queues = "ccQueue")
    public String process(String message) throws JsonProcessingException {

        ConversionInputDataDTO conversionInputDataDTO = new ObjectMapper().readValue(message, ConversionInputDataDTO.class);
        Conversion conversion = convert(convertToConversion(conversionInputDataDTO));
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

    /**
     * Метод конвертации
     *
     * @return объект конвертации с результатами конвертации (если они существуют)
     */
    private Conversion convert(Conversion conversion) {
        try { // получаем валютную ставку и, если она существует, конвертируем данные
            String rate = sendHttpRequestToGetCurrencyRate(conversion.getFromCurrency(), conversion.getToCurrency());
            if (rate != null) {
                conversion.setCurrencyRate(Double.parseDouble(rate));
                conversion.setTotalResult(calcConversionResult(conversion.getCurrencyRate(), conversion.getQuantity()));
                conversion.setConvertedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")));
            } else {
                log.debug("Failed to get data from Coingate REST API");
                conversion.setError("Выбранная валютная ставка не поддерживается!");
            }
        } catch (NumberFormatException e) {
            log.debug("Failed to parse the exchange rate");
            conversion.setError("Неподдерживаемый формат валютной ставки!");
        } catch (Exception e) {
            log.debug("The service for receiving a currency rate is unavailable!");
            conversion.setError("Сервис получения валютной ставки недоступен!");
        }
        return conversion; // конвертация с результатами
    }

    /**
     * Метод отправки GET HTTP запроса для получения валютной ставки
     *
     * @param from валюта, из которой конвертируют
     * @param to   валюта, в которую конвертируют
     * @return строковая валютная ставка
     */
    private String sendHttpRequestToGetCurrencyRate(String from, String to) throws
            HttpServerErrorException,
            HttpClientErrorException,
            UnknownHttpStatusCodeException {
        log.debug("Sends a GET HTTP request to get exchange rate from {} to {} from Coingate REST API", from, to);
        return new RestTemplate().getForObject(String.format("%s/%s/%s", this.url, from, to), String.class);
    }

    /**
     * Метод для конвертации суммы по валютной ставке
     *
     * @param rate     валютная ставка
     * @param quantity сумма
     * @return эквивалент суммы в другой валюте
     */
    private BigDecimal calcConversionResult(double rate, long quantity) {
        log.debug("Converts quantity {} using the received rate {}", rate, quantity);
        // умножаем сумму на валютную ставку
        BigDecimal result = new BigDecimal(rate).multiply(new BigDecimal(quantity));
        // если результат очень маленький, то кол-во знаков после запятой - 6, иначе - 2
        int scale = 2;
        if (result.compareTo(new BigDecimal(1)) <= 0) scale = 6;
        // округляем
        return result.setScale(scale, RoundingMode.FLOOR);
    }
}
