package PortalTADS.usuarioService.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Portal TADS - API de Usuários")
                        .version("1.0.0")
                        .description("""
                                API REST do microserviço de usuários do Portal TADS.
                                
                                Gerencia o cadastro, autenticação e manutenção de **usuários** e **professores**.
                                
                                ### Autenticação
                                A maioria dos endpoints requer um token JWT válido obtido via `POST /auth/login`.
                                Clique no botão **Authorize** e informe o token no formato: `Bearer <seu_token>`.
                                """)
                        .contact(new Contact()
                                .name("Equipe Portal TADS")
                                .email("contato@portaltads.com")
                        )
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desenvolvimento local")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Insira o token JWT obtido no endpoint de login")
                        )
                );
    }
}
