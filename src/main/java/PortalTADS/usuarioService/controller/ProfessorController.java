package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.enums.TitulacaoProfessor;
import PortalTADS.usuarioService.model.dto.professor.ProfessorRequestDTO;
import PortalTADS.usuarioService.model.dto.professor.ProfessorResponseDTO;
import PortalTADS.usuarioService.service.ProfessorService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/professores")
@AllArgsConstructor
@Tag(name = "Professores", description = "Gerenciamento de professores vinculados a cursos")
public class ProfessorController {

    private final ProfessorService professorService;

    @Operation(
            summary = "Cadastrar novo professor",
            description = """
                    Cria um novo professor no sistema, associando-o a um usuário e a um curso.
                    
                    **Permissões requeridas:** Papéis `ADMIN`, `PROFESSOR` ou `COORDENADOR`.
                    
                    O corpo da requisição inclui os dados do usuário (que será criado automaticamente)
                    e as informações específicas do professor (curso, titulação e descrição).
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Professor cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProfessorResponseDTO.class)),
                    headers = @Header(name = "Location", description = "URI do recurso criado", schema = @Schema(type = "string", example = "/professores/1"))
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
                    description = "Acesso negado — usuário não possui o papel necessário",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito — e-mail do professor já cadastrado no sistema",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> criarProfessor(@RequestBody @Valid ProfessorRequestDTO request) {

        ProfessorResponseDTO response = professorService.criarProfessor(request);
        URI localizacao = URI.create("/professores/" + response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).location(localizacao).body(response);
    }

    @Operation(
            summary = "Buscar professor por ID",
            description = "Retorna os dados completos de um professor específico, incluindo informações do usuário vinculado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Professor encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProfessorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Professor não encontrado para o ID fornecido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> buscarProfessorPorId(
            @Parameter(description = "ID único do professor", required = true, example = "1")
            @PathVariable Long id
    ) {
        ProfessorResponseDTO response = professorService.buscarProfessorPorId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar professores",
            description = """
                    Busca professores com filtros opcionais.
                    
                    - Se `nome` for informado, busca professores cujo nome contenha o termo.
                    - Se `titulacao` for informada, busca professores com a titulação especificada.
                    - Se nenhum filtro for informado, retorna todos os professores.
                    
                    Os filtros são mutuamente exclusivos — `nome` tem prioridade sobre `titulacao`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Busca realizada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProfessorResponseDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<ProfessorResponseDTO>> buscarProfessores(
            @Parameter(description = "Termo para buscar no nome do professor (parcial, case-insensitive)", example = "Maria")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Titulação acadêmica do professor para filtrar", example = "DOUTORADO")
            @RequestParam(required = false) TitulacaoProfessor titulacao
    ) {
        List<ProfessorResponseDTO> professores;
        if (nome != null) {
            professores = professorService.buscarProfessoresPorNome(nome);
        } else if (titulacao != null) {
            professores = professorService.buscarPorTitulacao(titulacao);
        } else {
            professores = professorService.listarProfessores();
        }
        return ResponseEntity.ok(professores);
    }

    @Operation(
            summary = "Atualizar professor",
            description = """
                    Atualiza os dados de um professor existente, incluindo os dados do usuário vinculado.
                    
                    **Permissões requeridas:** Papéis `PROFESSOR`, `ADMIN` ou `COORDENADOR`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Professor atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProfessorResponseDTO.class))
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
                    description = "Acesso negado — usuário não possui permissão para atualizar este recurso",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Professor não encontrado para atualização",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito — e-mail informado já pertence a outro usuário",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> atualizarProfessor(
            @Parameter(description = "ID único do professor a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody @Valid ProfessorRequestDTO request
    ) {
        ProfessorResponseDTO response = professorService.atualizarProfessor(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Atualizar perfil do professor autenticado",
            description = """
                    Atualiza os dados do perfil do professor que está autenticado no momento.
                    
                    O professor é identificado pelo e-mail presente no token JWT (campo `sub`).
                    Caso o token pertença a um usuário diferente do perfil a ser atualizado, a requisição
                    será rejeitada com **403 Forbidden**.
                    
                    **Permissão requerida:** Estar autenticado como o próprio professor.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil do professor atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProfessorResponseDTO.class))
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
                    description = "Acesso proibido — o token não pertence a este professor",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Professor não encontrado para o e-mail autenticado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito — e-mail informado já pertence a outro usuário",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PutMapping("/me")
    public ResponseEntity<ProfessorResponseDTO> atualizarPerfilProprio(
            Authentication authentication,
            @RequestBody @Valid ProfessorRequestDTO request
    ) {
        String subEmail = authentication.getName();
        return ResponseEntity.ok(professorService.atualizarPerfilProprio(subEmail, request));
    }

    @Operation(
            summary = "Deletar professor",
            description = """
                    Remove permanentemente um professor do sistema.
                    
                    **Permissões requeridas:** Papéis `ADMIN`, `PROFESSOR` ou `COORDENADOR`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Professor removido com sucesso (sem corpo na resposta)"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado — usuário não possui permissão necessária",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Professor não encontrado para exclusão",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProfessor(
            @Parameter(description = "ID único do professor a ser removido", required = true, example = "1")
            @PathVariable Long id
    ) {
        professorService.deletarProfessor(id);
        return ResponseEntity.noContent().build();
    }
}
