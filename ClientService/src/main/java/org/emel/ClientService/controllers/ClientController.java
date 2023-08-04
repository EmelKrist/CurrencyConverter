package org.emel.ClientService.controllers;

import org.emel.ClientService.dto.ConversionInputDataDTO;
import org.emel.ClientService.models.Conversion;
import org.emel.ClientService.services.ClientService;
import org.emel.ClientService.services.CurrenciesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;


/**
 * Rest контроллер для работы с клиентом
 */
@Controller
public class ClientController {
    private final ClientService clientService;
    private final CurrenciesService currenciesService;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientController(ClientService clientService, CurrenciesService currenciesService, ModelMapper modelMapper) {
        this.clientService = clientService;
        this.currenciesService = currenciesService;
        this.modelMapper = modelMapper;
    }

    /**
     * GET-запрос для получения формы главной страницы
     *
     * @param model модель представления
     * @return форма конвертации
     */
    @GetMapping()
    public String index(Model model) {
        // добавляем в модель представления список доступных валют и объект конвертации
        model.addAttribute("currencies", currenciesService.findAll());
        model.addAttribute("conversion", new Conversion());
        return "client/index"; // возвращаем представление
    }

    /**
     * POST-запрос для конвертации данных, полученных из формы
     *
     * @param conversion исходные данные конвертации из формы
     * @param model      модель представления
     * @return форма с результатом конвертации
     */
    @PostMapping()
    public String convert(@ModelAttribute("conversion") @Valid Conversion conversion,
                          BindingResult bindingResult, Model model) {
        model.addAttribute("currencies", currenciesService.findAll());
        if (bindingResult.hasErrors()) { // если есть ошибки валидации
            return "client/index";
        }
        // конвертируем данные, полученные из формы в объекте конвертации
        Optional<Conversion> conversionWithResult =
                clientService.execConversion(convertToConversionInputDataDTO(conversion));
        if (conversionWithResult.isPresent()) {
            // добавляем в модель представления объект конвертации с результатом
            model.addAttribute("conversion", conversionWithResult.get());
        } else { // иначе ошибка
            model.addAttribute("serviceError", clientService.getServiceError());
        }
        return "client/index";
    }

    /**
     * Метод конвертации объекта модели Conversion в объект ConversionInputDataDTO
     *
     * @param conversion объект модели Conversion
     * @return объект передачи данных сервису конвертации
     */
    private ConversionInputDataDTO convertToConversionInputDataDTO(Conversion conversion) {
        return modelMapper.map(conversion, ConversionInputDataDTO.class);
    }

}
