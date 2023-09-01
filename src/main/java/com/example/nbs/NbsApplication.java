package com.example.nbs;

import com.example.nbs.web.uploadingfiles.storage.StorageProperties;
import com.example.nbs.web.uploadingfiles.storage.StorageService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.example.nbs")
@EnableConfigurationProperties(StorageProperties.class)
@MapperScan("com.example.nbs.domain")
public class NbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NbsApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }


}
