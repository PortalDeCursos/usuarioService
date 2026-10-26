package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.model.dto.login.loginRequestDTO;
import PortalTADS.usuarioService.model.dto.login.loginResponseDTO;
import PortalTADS.usuarioService.service.autenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários e geração de tokens JWT")
public class autenticacaoController {

    private final autenticacaoService autenticacaoService;

    @Operation(
            summary = "Realizar login",
            description = """
                    Autentica o usuário com e-mail e senha e retorna um token JWT válido.
                    
                    O token retornado deve ser utilizado no header `Authorization` das demais requisições
                    no formato `Bearer <token>`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = loginResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de login inválidos (campos obrigatórios ausentes ou e-mail com formato inválido)",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais incorretas — e-mail ou senha não conferem",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<loginResponseDTO> login(@RequestBody @Valid loginRequestDTO loginRequestDTO) throws Exception {
        loginResponseDTO response = autenticacaoService.login(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

}
