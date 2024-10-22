package eu.trufchev.intargovishte;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableFeignClients
@SpringBootApplication
@OpenAPIDefinition
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class InTargovishteApplication {

	public static void main(String[] args) {
		SpringApplication.run(InTargovishteApplication.class, args);
	}

}
