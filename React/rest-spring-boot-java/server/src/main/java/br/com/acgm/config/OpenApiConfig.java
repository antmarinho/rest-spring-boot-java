package br.com.acgm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration //SWAGGER
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI() {
		
		return new OpenAPI()
				.info(new Info()
				.title("RESTfull API with JAVA 19 and Spring Boot 3.0.1")
				.version("v1")
				.description("alguem descricao sobre a API")
				.termsOfService("links dos termos de servicos")
				.license(new License()
						.name("Apache 2.0")
						.url("links da licenca"))
				);
	}
}
