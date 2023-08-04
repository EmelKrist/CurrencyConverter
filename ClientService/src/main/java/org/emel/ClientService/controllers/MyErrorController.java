package org.emel.ClientService.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Контроллер для маппинга страниц с ошибками
 */
@Controller
@RequestMapping("/error")
public class MyErrorController implements ErrorController {
    public String handleError() {
        return "error";
    }

    /**
     * Метод обработки ошибок
     *
     * @param request запрос ошибки
     * @return представление с сообщением об ошибке
     */
    @RequestMapping()
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // получаем статус ошибки и возвращаем представление, в зависимости от кода
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "errors/error-404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "errors/error-500";
            }
        }
        return "errors/error";
    }
}
