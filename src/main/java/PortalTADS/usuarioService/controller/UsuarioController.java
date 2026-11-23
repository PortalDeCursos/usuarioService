package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.model.dto.usuario.UsuarioRequestDTO;
import PortalTADS.usuarioService.model.dto.usuario.UsuarioResponseDTO;
import PortalTADS.usuarioService.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

//////
@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema (CRUD completo)")
public class UsuarioController {

    private final UsuarioService service;

    @Operation(
            summary = "Cadastrar novo usuário",
            description = """
                    Cria um novo usuário no sistema.
                    
                    Este endpoint é **público** e não requer autenticação. É utilizado para
                    o auto-cadastro de novos usuários na plataforma.
                    
                    O usuário criado recebe o papel padrão `USER`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)),
                    headers = @Header(name = "Location", description = "URI do recurso criado", schema = @Schema(type = "string", example = "/usuarios/1"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos — campos obrigatórios ausentes ou com formato incorreto",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito — e-mail já cadastrado no sistema",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @SecurityRequirements
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvarUsuario(@RequestBody @Valid UsuarioRequestDTO record) {
        UsuarioResponseDTO response = service.salvarUsuario(record);
        URI location = URI.create("/usuarios/" + response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
    }

    @Operation(
            summary = "Buscar usuário por ID",
            description = """
                    Retorna os dados cadastrais de um usuário específico a partir do seu identificador único.
                    
                    **Permissões requeridas:** Papel `ADMIN`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado — requer papel ADMIN",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado para o ID informado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuario(
            @Parameter(description = "ID único do usuário", required = true, example = "1")
            @PathVariable Long id
    ) {
        UsuarioResponseDTO response = service.buscarUsuario(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Listar todos os usuários",
            description = """
                    Retorna a lista completa de todos os usuários cadastrados no sistema.
                    
                    **Permissões requeridas:** Papel `ADMIN`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuários retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado — requer papel ADMIN",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> response = service.listarUsuarios();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Buscar usuários por nome",
            description = """
                    Busca usuários cujo nome contenha o termo informado.
                    
                    Se o parâmetro `nome` não for fornecido, retorna todos os usuários.
                    
                    **Permissões requeridas:** Papel `ADMIN`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Busca realizada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado — requer papel ADMIN",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarUsuariosPorNome(
            @Parameter(description = "Termo para buscar no nome do usuário (parcial, case-insensitive)", example = "João")
            @RequestParam(required = false) String nome
    ) {
        List<UsuarioResponseDTO> response;
        if (nome != null) {
            response = service.buscarUsuariosPorNome(nome);
        } else {
            response = service.listarUsuarios();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Atualizar usuário",
            description = """
                    Atualiza os dados de um usuário existente. Todos os campos do corpo da requisição são obrigatórios.
                    
                    **Permissões requeridas:** Papel `ADMIN`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos — campos obrigatórios ausentes ou com formato incorreto",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado — requer papel ADMIN",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito — e-mail já pertence a outro usuário",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PutMapping("/{id}")

    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
            @Parameter(description = "ID único do usuário a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody @Valid UsuarioRequestDTO record
    ) {
        UsuarioResponseDTO response = service.atualizarUsuario(id, record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Deletar usuário",
            description = """
                    Remove permanentemente um usuário do sistema pelo seu ID.
                    
                    **Permissões requeridas:** Papel `ADMIN`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário removido com sucesso (sem corpo na resposta)"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado — requer papel ADMIN",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado para exclusão",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(
            @Parameter(description = "ID único do usuário a ser removido", required = true, example = "1")
            @PathVariable Long id
    ) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualizar perfil do usuário autenticado",
            description = """
                    Atualiza os dados do perfil do usuário que está autenticado no momento.
                    
                    O usuário é identificado pelo e-mail presente no token JWT (campo `sub`).
                    Caso o token pertença a um usuário diferente do perfil a ser atualizado, a requisição
                    será rejeitada com **403 Forbidden**.
                    
                    **Permissão requerida:** Estar autenticado como o próprio usuário.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil do usuário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos — campos obrigatórios ausentes ou incorretos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso proibido — o token não pertence a este usuário",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado para o e-mail autenticado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito — e-mail informado já pertence a outro usuário",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> atualizarPerfilProprio(
            Authentication authentication,
            @RequestBody @Valid UsuarioRequestDTO request
    ) {
        String subEmail = authentication.getName();
        return ResponseEntity.ok(service.atualizarPerfilProprio(subEmail, request));
    }
}
