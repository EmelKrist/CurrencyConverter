package org.emel.CurrencyConversionService.controllers;

import org.emel.CurrencyConversionService.dto.ConversionDTO;
import org.emel.CurrencyConversionService.dto.ConversionInputDataDTO;
import org.emel.CurrencyConversionService.dto.ConversionsResponse;
import org.emel.CurrencyConversionService.models.Conversion;
import org.emel.CurrencyConversionService.services.ConversionsService;
import org.emel.CurrencyConversionService.util.CurrencyRateIsNotSupported;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST контроллер для работы с конвертациями
 */
@RestController
@RequestMapping("/conversions")
public class ConversionsController {

    private final Logger log = LoggerFactory.getLogger(ConversionsController.class);
    private final ConversionsService conversionsService;
    private final ModelMapper modelMapper;

    @Autowired
    public ConversionsController(ConversionsService conversionsService,
                                 ModelMapper modelMapper) {
        this.conversionsService = conversionsService;
        this.modelMapper = modelMapper;
    }

    /**
     * GET-запрос на получение списка всех конвертаций (DTO)
     *
     * @return ответ со списком конвертаций в JSON формате
     */
    @GetMapping()
    public ResponseEntity<ConversionsResponse> getAll() {
        log.debug("Gets REST request to receive all data");
        return ResponseEntity.ok(
                new ConversionsResponse(
                        conversionsService.findAll()
                                .stream().map(this::convertToConversionDTO)
                                .collect(Collectors.toList())));
    }

    /**
     * POST-запрос на конвертацию
     *
     * @param conversionInputDataDTO данные конвертации от клиента
     * @return ответ с результатом конвертации в json формате
     */
    @PostMapping()
    public ResponseEntity<ConversionDTO> convert(@RequestBody ConversionInputDataDTO conversionInputDataDTO) {
        log.debug("Gets REST request to convert data : {}", conversionInputDataDTO);
        // конвертируем входные данные
        Optional<Conversion> conversion = conversionsService.convert(convertToConversion(conversionInputDataDTO));
        // если конвертация получена(существует), сохраняем в БД и возвращаем результат клиенту
        if (conversion.isPresent()) {
            conversionsService.save(conversion.get());
            return ResponseEntity.ok().body(convertToConversionDTO(conversion.get()));
        } else { // в противном случае указываем, что заданная конвертация невозможна
            log.debug("Failed to get data from Coingate REST API");
            throw new CurrencyRateIsNotSupported("Выбранная валютная ставка не поддерживается!");
        }
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
     * Метод конвертации объекта модели Conversion в объект ConversionDTO
     *
     * @param conversion объект модели Conversion
     * @return объект передачи данных клиенту
     */
    private ConversionDTO convertToConversionDTO(Conversion conversion) {
        return modelMapper.map(conversion, ConversionDTO.class);
    }

}
