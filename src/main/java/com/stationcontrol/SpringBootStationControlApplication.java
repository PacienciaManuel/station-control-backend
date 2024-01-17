package com.stationcontrol;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.stationcontrol.storage.StorageService;

@SpringBootApplication
public class SpringBootStationControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStationControlApplication.class, args);
	}
	
	@Bean
	CommandLineRunner initStorage(StorageService storageService) {
		return args -> storageService.init();
	}

}
