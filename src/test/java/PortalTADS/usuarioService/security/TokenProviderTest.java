    package PortalTADS.usuarioService.security;

    import PortalTADS.usuarioService.enums.roleUsuario;
    import PortalTADS.usuarioService.exception.TokenGeracaoException;
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

    // Asserções padrão do JUnit 5 Jupiter
    import static org.junit.jupiter.api.Assertions.*;

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

            // 3. Verificações usando asserções nativas do JUnit 5 (Assertions):
            assertNotNull(token, "O token gerado não deve ser nulo");
            assertFalse(token.isBlank(), "O token gerado não deve estar vazio");

            // Validar a assinatura criptográfica usando a chave secreta com o parser do JJWT
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
            assertEquals("admin@portaltads.com", claims.getSubject(), "O subject deve ser o email do admin");
            assertEquals("ADMIN", claims.get("role", String.class), "A role no token deve ser ADMIN");

            // Validar expiração e emissão
            Date agora = new Date();
            assertTrue(claims.getIssuedAt().compareTo(agora) <= 0, "A data de emissão deve ser anterior ou igual ao momento atual");
            assertTrue(claims.getExpiration().after(agora), "A data de expiração deve ser futura");
        }

        @Test
        @DisplayName("Deve lançar TokenGeracaoException quando o objeto Authentication for nulo")
        void deveLancarExcecaoQuandoAuthenticationForNulo() {
            TokenGeracaoException exception = assertThrows(
                    TokenGeracaoException.class,
                    () -> tokenProvider.gerarToken(null),
                    "Deve lançar TokenGeracaoException quando authentication for nulo"
            );

            assertEquals("A autenticação e o usuário principal não podem ser nulos", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar TokenGeracaoException quando o principal da autenticação for nulo")
        void deveLancarExcecaoQuandoPrincipalForNulo() {
            Authentication authComPrincipalNulo = new UsernamePasswordAuthenticationToken(null, null);

            TokenGeracaoException exception = assertThrows(
                    TokenGeracaoException.class,
                    () -> tokenProvider.gerarToken(authComPrincipalNulo),
                    "Deve lançar TokenGeracaoException quando o principal for nulo"
            );

            assertEquals("A autenticação e o usuário principal não podem ser nulos", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar TokenGeracaoException quando o principal não for uma instância de userDetails")
        void deveLancarExcecaoQuandoPrincipalForInvalido() {
            Authentication authInvalida = new UsernamePasswordAuthenticationToken("usuario_invalido", "senha");

            TokenGeracaoException exception = assertThrows(
                    TokenGeracaoException.class,
                    () -> tokenProvider.gerarToken(authInvalida),
                    "Deve lançar TokenGeracaoException quando o principal não for userDetails"
            );

            assertEquals("O principal da autenticação deve ser uma instância de userDetails válida", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar TokenGeracaoException quando o usuário tiver username ou role nulos")
        void deveLancarExcecaoQuandoDadosDoUsuarioForemInvalidos() {
            usuario usuarioInvalido = new usuario();
            usuarioInvalido.setEmail(null);
            usuarioInvalido.setTipoUsuario(roleUsuario.ALUNO);

            userDetails userDetails = new userDetails(usuarioInvalido);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "senha");

            TokenGeracaoException exception = assertThrows(
                    TokenGeracaoException.class,
                    () -> tokenProvider.gerarToken(authentication),
                    "Deve lançar TokenGeracaoException quando username for nulo"
            );

            assertEquals("O username/e-mail do usuário não pode ser nulo ou vazio", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar TokenGeracaoException quando a chave secreta for nula ou vazia")
        void deveLancarExcecaoQuandoChaveSecretaForNulaOuVazia() {
            usuario admin = new usuario(1L, "Admin", "admin@portaltads.com", "123456", roleUsuario.ADMIN);
            userDetails userDetails = new userDetails(admin);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "123456", userDetails.getAuthorities());

            // Teste com segredo nulo
            ReflectionTestUtils.setField(tokenProvider, "segredo", null);
            TokenGeracaoException exNula = assertThrows(
                    TokenGeracaoException.class,
                    () -> tokenProvider.gerarToken(authentication)
            );
            assertEquals("A chave secreta (Secret Key) não pode ser nula ou vazia", exNula.getMessage());

            // Teste com segredo vazio / em branco
            ReflectionTestUtils.setField(tokenProvider, "segredo", "   ");
            TokenGeracaoException exVazia = assertThrows(
                    TokenGeracaoException.class,
                    () -> tokenProvider.gerarToken(authentication)
            );
            assertEquals("A chave secreta (Secret Key) não pode ser nula ou vazia", exVazia.getMessage());
        }

        @Test
        @DisplayName("Deve lançar TokenGeracaoException quando a chave secreta for insuficiente ou corrompida")
        void deveLancarExcecaoQuandoChaveSecretaForInvalidaOuCorrompida() {
            usuario admin = new usuario(1L, "Admin", "admin@portaltads.com", "123456", roleUsuario.ADMIN);
            userDetails userDetails = new userDetails(admin);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "123456", userDetails.getAuthorities());

            // Chave curta demais para HMAC-SHA (menos de 256 bits / 32 bytes)
            ReflectionTestUtils.setField(tokenProvider, "segredo", "chave_curta_invalida");

            TokenGeracaoException exception = assertThrows(
                    TokenGeracaoException.class,
                    () -> tokenProvider.gerarToken(authentication),
                    "Deve lançar TokenGeracaoException quando a chave for insuficiente para HMAC-SHA"
            );

            assertTrue(
                    exception.getMessage().contains("A chave secreta fornecida é inválida ou insuficiente para assinatura HMAC-SHA"),
                    "A mensagem deve alertar sobre chave inválida ou insuficiente"
            );
        }
    }
