package baitap10;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import baitap10.config.StorageProperties;
import baitap10.service.IStorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Baitap10Application {

    public static void main(String[] args) {
        SpringApplication.run(Baitap10Application.class, args);
    }

    @Bean
    CommandLineRunner init(IStorageService storageService) {
        return args -> storageService.init();
    }
}