package com.example.shoeMash;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShoeMashApplication {

    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images"; // Your images folder

    public static void main(String[] args) {
        SpringApplication.run(ShoeMashApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(ShoeService shoeService) {
        return args -> {
            // Dynamically load all images as shoes
            shoeService.loadShoesFromImages(IMAGE_DIRECTORY);
        };
    }
}
