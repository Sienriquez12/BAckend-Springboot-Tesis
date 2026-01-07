package com.especlub.match.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ESPE  CLUBS MATCH API",
                version = "1.0",
                description = """
                    Proyecto  para encontrar clubs para estudiantes de la ESPE
                    """
        ),
        servers = @Server(url = "http://api.especlubsmatch.app/")
)
public class OpenApiConfig {
}