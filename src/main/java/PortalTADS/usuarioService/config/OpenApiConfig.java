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
                                ## Microsserviço de Gerenciamento de Usuários
                                
                                Esta API REST é responsável pelo ecossistema de autenticação, usuários e perfis educacionais do **Portal TADS**.
                                
                                ### Módulos e Funcionalidades:
                                - 🔐 **Autenticação**: Emissão de tokens JWT via login seguro.
                                - 👤 **Usuários**: CRUD completo de contas de acesso ao sistema com papéis (`ADMIN`, `PROFESSOR`, `COORDENADOR`, `ALUNO`).
                                - 👨‍🏫 **Professores**: Gestão do corpo docente vinculado a cursos e titulações acadêmicas.
                                - 🎓 **Alunos**: Cadastro e acompanhamento de discentes vinculados a cursos.
                                - 📋 **Coordenadores**: Gestão de coordenação de cursos integrada aos professores.
                                
                                ---
                                
                                ### 🔑 Guia de Autenticação
                                1. Acesse o endpoint **`POST /auth/login`** com suas credenciais.
                                2. Copie o token retornado no campo `token`.
                                3. Clique no botão verde **Authorize** no topo direito do Swagger.
                                4. Cole o token (apenas o token JWT ou com prefixo `Bearer`).
                                """)
                        .contact(new Contact()
                                .name("Suporte Portal TADS")
                                .email("contato@portaltads.com")
                                .url("https://portaltads.com")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Ambiente Local / Desenvolvimento"),
                        new Server()
                                .url("https://api.portaltads.com")
                                .description("Ambiente de Produção (Exemplo)")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Informe o token JWT gerado pelo endpoint `/auth/login`.")
                        )
                );
    }
}
