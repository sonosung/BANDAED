package org.astrologist.midea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MideaApplication {

	public static void main(String[] args) {

		SpringApplication.run(MideaApplication.class, args);
	}

}
