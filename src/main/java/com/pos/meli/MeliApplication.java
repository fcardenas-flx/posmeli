package com.pos.meli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class MeliApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(MeliApplication.class, args);
	}

}
