package org.emel.ClientService;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClientServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
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
