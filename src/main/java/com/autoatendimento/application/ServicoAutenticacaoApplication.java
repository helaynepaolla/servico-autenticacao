package com.autoatendimento.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.autoatendimento")
@EnableJpaRepositories(basePackages = "com.autoatendimento.auth.repository")
@EntityScan(basePackages = "com.autoatendimento.auth.entity")
public class ServicoAutenticacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicoAutenticacaoApplication.class, args);
	}

}
