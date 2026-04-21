package br.com.fiap.checkpoint4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class Checkpoint4Application {

	public static void main(String[] args) {
		SpringApplication.run(Checkpoint4Application.class, args);
	}

}
