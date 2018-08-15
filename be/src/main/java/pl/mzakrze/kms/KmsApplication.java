package pl.mzakrze.kms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class KmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KmsApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		// FIXME - zmienić secret i wynieść
		return new BCryptPasswordEncoder();
	}
}
