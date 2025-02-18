package com.javaPpmTool.ppmtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PpmtoolApplication {

	// create the bean here so that we can auto wire in UserService.java if bCryptPasswordEncoder cannot be autowired
	// The BCryptPasswordEncoder implementation uses the widely supported bcrypt algorithm to encode the passwords
	@Bean
	public static PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(PpmtoolApplication.class, args);
	}

}
