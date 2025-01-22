package com.InfoSec.dynamic_password;

import com.InfoSec.dynamic_password.global.security.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
@ConfigurationPropertiesScan
public class DynamicPasswordApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicPasswordApplication.class, args);
	}

}
