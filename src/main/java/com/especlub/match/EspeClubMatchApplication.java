package com.especlub.match;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
@EnableAsync
public class EspeClubMatchApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        // add DB config from .env
        System.setProperty("DB_URL", dotenv.get("DB_URL", ""));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME", ""));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD", ""));
        System.setProperty("SECRET_KEY_JWT", dotenv.get("SECRET_KEY_JWT", ""));
        System.setProperty("EXPIRATION_TIME_JWT", dotenv.get("EXPIRATION_TIME_JWT", ""));

        // emails config
        System.setProperty("ALERT_SMTP_HOST", dotenv.get("ALERT_SMTP_HOST", ""));
        System.setProperty("ALERT_SMTP_FROM", dotenv.get("ALERT_SMTP_FROM", ""));
        System.setProperty("ALERT_SMTP_USER", dotenv.get("ALERT_SMTP_USER", ""));
        System.setProperty("ALERT_SMTP_PASSWORD", dotenv.get("ALERT_SMTP_PASSWORD", ""));
        System.setProperty("ALERT_SMTP_TO", dotenv.get("ALERT_SMTP_TO", ""));
        System.setProperty("ALERT_SMTP_PORT", dotenv.get("ALERT_SMTP_PORT", ""));
        System.setProperty("API_GEMINI_KEY", dotenv.get("API_GEMINI_KEY", ""));


        SpringApplication.run(EspeClubMatchApplication.class, args);
    }
}
