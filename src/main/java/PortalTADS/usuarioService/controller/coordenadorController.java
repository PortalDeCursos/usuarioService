package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.model.dto.coordenador.coordenadorRequestDTO;
import PortalTADS.usuarioService.model.dto.coordenador.coordenadorResponseDTO;
import PortalTADS.usuarioService.service.coordenadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RestController
@RequestMapping("/coordenadores")
@AllArgsConstructor
@Tag(name = "Coordenadores", description = "Endpoints para gerenciamento de coordenadores vinculados a cursos (CRUD e buscas)")
public class coordenadorController {

    private final coordenadorService coordenadorService;

    @Operation(
            summary = "Cadastrar novo coordenador",
            description = """
                    Cria um novo registro de coordenador associando um professor já existente a um curso específico.
                    
                    **Permissões requeridas:** Papel `ADMIN`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Coordenador cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = coordenadorResponseDTO.class)),
                    headers = @Header(name = "Location", description = "URI do recurso criado", schema = @Schema(type = "string", example = "/coordenadores/1"))
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
                    description = "Acesso negado — usuário não possui papel ADMIN",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito — professor já está cadastrado como coordenador",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<coordenadorResponseDTO> criarCoordenador(@RequestBody @Valid coordenadorRequestDTO request) {
        coordenadorResponseDTO response = coordenadorService.criarCoordenador(request);
        URI location = URI.create("/coordenadores/" + response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
    }

    @Operation(
            summary = "Buscar coordenador por ID",
            description = """
                    Retorna os dados completos de um coordenador específico, incluindo os dados do professor vinculado.
                    
                    **Permissões requeridas:** Papéis `ADMIN` ou `COORDENADOR`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Coordenador encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = coordenadorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado — requer papel ADMIN ou COORDENADOR",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Coordenador não encontrado para o ID fornecido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COORDENADOR')")
    public ResponseEntity<coordenadorResponseDTO> buscarCoordenadorPorId(
            @Parameter(description = "ID numérico único do coordenador", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(coordenadorService.buscarCoordenadorPorId(id));
    }

    @Operation(
            summary = "Buscar ou listar coordenadores",
            description = """
                    Busca coordenadores através de filtros opcionais:
                    
                    - Se `nome` for informado, filtra por nome do professor/coordenador.
                    - Se `cursoId` for informado (e nome ausente), filtra pelo ID do curso.
                    - Se nenhum parâmetro for informado, lista todos os coordenadores cadastrados.
                    
                    **Permissões requeridas:** Papéis `ADMIN` ou `COORDENADOR`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de coordenadores retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = coordenadorResponseDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado — requer papel ADMIN ou COORDENADOR",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COORDENADOR')")
    public ResponseEntity<List<coordenadorResponseDTO>> buscarCoordenadores(
            @Parameter(description = "Filtro parcial por nome do coordenador (case-insensitive)", example = "Maria")
            @RequestParam(required = false) String nome,
            @Parameter(description = "ID do curso para filtrar o coordenador responsável", example = "1")
            @RequestParam(required = false) Long cursoId
    ) {
        List<coordenadorResponseDTO> coordenadores;
        if (nome != null) {
            coordenadores = coordenadorService.buscarCoordenadoresPorNome(nome);
        } else if (cursoId != null) {
            coordenadores = coordenadorService.buscarPorCurso(cursoId);
        } else {
            coordenadores = coordenadorService.listarCoordenadores();
        }
        return ResponseEntity.ok(coordenadores);
    }

    @Operation(
            summary = "Atualizar coordenador",
            description = """
                    Atualiza o professor vinculado e/ou o curso de um coordenador existente.
                    
                    **Permissões requeridas:** Papéis `ADMIN` ou `COORDENADOR`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Coordenador atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = coordenadorResponseDTO.class))
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
                    description = "Acesso negado — requer papel ADMIN ou COORDENADOR",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Coordenador não encontrado para atualização",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COORDENADOR')")
    public ResponseEntity<coordenadorResponseDTO> atualizarCoordenador(
            @Parameter(description = "ID único do coordenador a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody @Valid coordenadorRequestDTO request
    ) {
        return ResponseEntity.ok(coordenadorService.atualizarCoordenador(id, request));
    }

    @Operation(
            summary = "Remover coordenador",
            description = """
                    Remove permanentemente um coordenador do sistema pelo seu identificador.
                    
                    **Permissões requeridas:** Apenas papel `ADMIN`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Coordenador removido com sucesso (sem corpo na resposta)"
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
                    description = "Coordenador não encontrado para exclusão",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarCoordenador(
            @Parameter(description = "ID único do coordenador a ser removido", required = true, example = "1")
            @PathVariable Long id
    ) {
        coordenadorService.deletarCoordenador(id);
        return ResponseEntity.noContent().build();
    }
}
