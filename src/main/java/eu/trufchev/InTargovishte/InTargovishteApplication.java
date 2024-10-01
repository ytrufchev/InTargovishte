package eu.trufchev.InTargovishte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class InTargovishteApplication {

	public static void main(String[] args) {
		SpringApplication.run(InTargovishteApplication.class, args);
	}

}
