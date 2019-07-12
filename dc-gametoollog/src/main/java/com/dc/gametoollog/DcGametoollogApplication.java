package com.dc.gametoollog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dc.gametoollog.conf.MakeLogProperties;

@SpringBootApplication
@EnableConfigurationProperties({MakeLogProperties.class})
public class DcGametoollogApplication {

	public static void main(String[] args) {
		SpringApplication.run(DcGametoollogApplication.class, args);
	}

}
