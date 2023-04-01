package org.emel.CurrencyConversionService;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CurrencyConversionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyConversionServiceApplication.class, args);
	}

	/**
	 * Бин для внедрения конвертера моделей
	 * @return новый объект для отображения моделей
	 */
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
