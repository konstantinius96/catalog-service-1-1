package ru.samsebemehanik.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CatalogService11Application {

    public static void main(String[] args) {
        SpringApplication.run(CatalogService11Application.class, args);
    }

}
