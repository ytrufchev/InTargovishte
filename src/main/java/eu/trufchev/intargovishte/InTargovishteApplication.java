package eu.trufchev.intargovishte;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@SpringBootApplication
@OpenAPIDefinition
@EnableScheduling
public class InTargovishteApplication {

	public static void main(String[] args) {
		SpringApplication.run(InTargovishteApplication.class, args);
	}

}
