package com.mballem.curso.security.udemyconsultamedico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class UdemyConsultaMedicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(UdemyConsultaMedicoApplication.class, args);
	}

}
