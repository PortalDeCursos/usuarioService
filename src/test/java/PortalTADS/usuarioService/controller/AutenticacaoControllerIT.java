package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.model.dto.login.loginRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AutenticacaoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve realizar login com sucesso quando o administrador informar credenciais válidas")
    void deveRealizarLoginComSucessoQuandoCredenciaisDoAdminForemValidas() throws Exception {
        loginRequestDTO request = new loginRequestDTO("admin@portaltads.com", "suasenha");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.token", not(emptyString())))
                .andExpect(jsonPath("$.role", is("ADMIN")))
                .andExpect(jsonPath("$.expiracao", notNullValue()));
    }

    @Test
    @DisplayName("Deve retornar 401 quando o usuário informar um e-mail incorreto")
    void deveRetornar401QuandoEmailForIncorreto() throws Exception {

        loginRequestDTO request =
                new loginRequestDTO("errado@portaltads.com", "suasenha");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.valueOf("application/problem+json")
                ))
                .andExpect(jsonPath("$.detail")
                        .value("Credenciais incorretas — e-mail ou senha não conferem"))
                .andExpect(jsonPath("$.instance")
                        .value("/auth/login"))
                .andExpect(jsonPath("$.status")
                        .value(401))
                .andExpect(jsonPath("$.title")
                        .value("Falha na autenticação"))
                .andExpect(jsonPath("$.timestamp")
                        .exists());
    }
}
