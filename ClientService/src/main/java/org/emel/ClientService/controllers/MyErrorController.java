package org.emel.ClientService.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для маппинга страниц с ошибками
 */
@Controller
@RequestMapping("/error")
public class MyErrorController implements ErrorController {
    public String handleError() {
        return "error";
    }
}
