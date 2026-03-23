package fadesp.java.TesteTecnico_Paulo.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Fadesp - API de Pagamentos")
                                                .version("1.0.0")
                                                .description("""
                                                                Documentação da API REST para recebimento e gestão de status de pagamentos.
                                                                """)
                                                .contact(new Contact()
                                                                .name("Email: Paulo Moraes")
                                                                .email("paulompj010@gmail.com")))
                                .externalDocs(new ExternalDocumentation()
                                                .description("Link para o Repositório no GitHub")
                                                .url("https://github.com/Paulompj/Fadesp-Desafio"));
        }
}