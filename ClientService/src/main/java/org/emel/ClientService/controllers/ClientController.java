package org.emel.ClientService.controllers;

import org.emel.ClientService.services.ClientService;
import org.emel.ClientService.services.CurrenciesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest контроллер для работы с клиентом
 */
@RestController
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
}
