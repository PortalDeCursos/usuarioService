package PortalTADS.usuarioService.security;

import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.model.usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TokenProviderTest {

    private tokenProvider tokenProvider;

    private final String segredo = "14I15oujcYSvV7r7Lghmg+HUMJ5n8YXDYmQoyHFiWMA=";
    private final long tempoExpiracao = 36000000L; // 10 horas em ms

    @BeforeEach
    void setUp() {
        tokenProvider = new tokenProvider();
        // Injeta os valores das propriedades simulando o application.properties / .env
        ReflectionTestUtils.setField(tokenProvider, "segredo", segredo);
        ReflectionTestUtils.setField(tokenProvider, "tempoExpiracao", tempoExpiracao);
    }

    @Test
    @DisplayName("Deve gerar token JWT com sucesso para um usuário ADMIN e validar assinatura e role")
    void deveGerarTokenAdminComSucessoEValidarAssinaturaERole() {
        // 1. Cenário: criação do usuário ADMIN com email e senha fornecidos
        usuario admin = new usuario();
        admin.setId(1L);
        admin.setNome("Administrador");
        admin.setEmail("admin@portaltads.com");
        admin.setSenha("suasenha");
        admin.setTipoUsuario(roleUsuario.ADMIN);

        userDetails userDetails = new userDetails(admin);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                admin.getSenha(),
                userDetails.getAuthorities()
        );

        // 2. Ação: gerar o token JWT
        String token = tokenProvider.gerarToken(authentication);

        // 3. Verificações:
        // Token não nulo e preenchido
        assertThat(token).isNotNull().isNotBlank();

        // Validar a assinatura criptográfica usando a mesma chave secreta
        SecretKey secretKey = Keys.hmacShaKeyFor(segredo.getBytes());

        Claims claims = assertDoesNotThrow(() ->
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload(),
                "A assinatura do token deve ser válida de acordo com a chave secreta"
        );

        // Validar dados contidos no payload (Claims)
        assertThat(claims.getSubject()).isEqualTo("admin@portaltads.com");
        assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");

        // Validar expiração e emissão
        assertThat(claims.getIssuedAt()).isBeforeOrEqualTo(new Date());
        assertThat(claims.getExpiration()).isAfter(new Date());
    }
}

