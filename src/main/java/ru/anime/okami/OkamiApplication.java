package ru.anime.okami;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.anime.okami.utils.RsaProperties;

import java.io.IOException;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.anime.okami")
@EnableConfigurationProperties(RsaProperties.class)
public class OkamiApplication {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(OkamiApplication.class, args);
    }

}
