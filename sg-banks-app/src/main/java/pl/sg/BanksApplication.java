package pl.sg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.sg.banks.go_cardless.GoCardlessTokenCache;
import pl.sg.go_cardless.service.GoCardlessClient;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
public class BanksApplication {

    public static void main(String[] args) {
        SpringApplication.run(BanksApplication.class, args);
    }

    @Bean
    GoCardlessClient goCardlessClient(
            Configuration configuration,
            @Value("${go-cardless.service-url}") String serviceUrl,
            GoCardlessTokenCache goCardlessTokenCache) {
        return new GoCardlessClient(
                goCardlessTokenCache,
                serviceUrl,
                configuration.getNordigenSecretId(),
                configuration.getNordigenSecretKey(),
                LocalDateTime::now
        );
    }
}
