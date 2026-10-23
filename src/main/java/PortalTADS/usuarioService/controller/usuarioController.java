package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.model.dto.usuario.usuarioRequestDTO;
import PortalTADS.usuarioService.model.dto.usuario.usuarioResponseDTO;
import PortalTADS.usuarioService.service.usuarioService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

//////
@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema (CRUD completo)")
public class usuarioController {

    private final usuarioService service;

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
                    content = @Content(schema = @Schema(implementation = usuarioResponseDTO.class)),
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<usuarioResponseDTO> salvarUsuario(@RequestBody @Valid usuarioRequestDTO record) {
        usuarioResponseDTO response = service.salvarUsuario(record);
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
                    content = @Content(schema = @Schema(implementation = usuarioResponseDTO.class))
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<usuarioResponseDTO> buscarUsuario(
            @Parameter(description = "ID único do usuário", required = true, example = "1")
            @PathVariable Long id
    ) {
        usuarioResponseDTO response = service.buscarUsuario(id);
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
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = usuarioResponseDTO.class)))
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<usuarioResponseDTO>> listarUsuarios() {
        List<usuarioResponseDTO> response = service.listarUsuarios();
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
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = usuarioResponseDTO.class)))
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
    @PreAuthorize( "hasRole('ADMIN')")
    public ResponseEntity<List<usuarioResponseDTO>> buscarUsuariosPorNome(
            @Parameter(description = "Termo para buscar no nome do usuário (parcial, case-insensitive)", example = "João")
            @RequestParam(required = false) String nome
    ) {
        List<usuarioResponseDTO> response;
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
                    content = @Content(schema = @Schema(implementation = usuarioResponseDTO.class))
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
    @PreAuthorize( "hasRole('ADMIN')")
    public ResponseEntity<usuarioResponseDTO> atualizarUsuario(
            @Parameter(description = "ID único do usuário a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody @Valid usuarioRequestDTO record
    ) {
        usuarioResponseDTO response = service.atualizarUsuario(id, record);
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
    @PreAuthorize( " hasRole('ADMIN')")
    public ResponseEntity<Void> deletarUsuario(
            @Parameter(description = "ID único do usuário a ser removido", required = true, example = "1")
            @PathVariable Long id
    ) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
