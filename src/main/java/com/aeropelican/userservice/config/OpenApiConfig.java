package com.aeropelican.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userServiceOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8081");
        localServer.setDescription("Local Environment");

        Contact contact = new Contact();
        contact.setName("AeroPelican Engineering");
        contact.setEmail("support@aeropelican.com");

        Info info = new Info()
                .title("User Service REST API")
                .version("1.0.0")
                .contact(contact)
                .description("Microservice for user registration, authentication, address management, and role management.")
                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"));

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}