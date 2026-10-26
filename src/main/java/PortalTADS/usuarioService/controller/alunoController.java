package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.model.dto.aluno.alunoRequestDTO;
import PortalTADS.usuarioService.model.dto.aluno.alunoResponseDTO;
import PortalTADS.usuarioService.service.alunoService;
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

@RestController
@RequestMapping("/alunos")
@AllArgsConstructor
@Tag(name = "Alunos", description = "Endpoints para gerenciamento de alunos vinculados a cursos (CRUD e buscas)")
public class alunoController {

    private final alunoService alunoService;

    @Operation(
            summary = "Cadastrar novo aluno",
            description = """
                    Cria um novo registro de aluno no sistema, associando-o a um usuário e a um curso.
                    
                    Este endpoint é **público** e permite o auto-cadastro ou registro de discentes.
                    O usuário associado receberá as credenciais e vínculo com o respectivo ID do curso informado.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Aluno cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = alunoResponseDTO.class)),
                    headers = @Header(name = "Location", description = "URI do recurso criado", schema = @Schema(type = "string", example = "/alunos/1"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos — campos obrigatórios ausentes ou com formato incorreto",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito — e-mail do aluno já cadastrado no sistema",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @SecurityRequirements
    @PostMapping
    public ResponseEntity<alunoResponseDTO> criarAluno(@RequestBody @Valid alunoRequestDTO request) {
        alunoResponseDTO response = alunoService.criarAluno(request);
        URI location = URI.create("/alunos/" + response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
    }

    @Operation(
            summary = "Buscar aluno por ID",
            description = """
                    Retorna os dados cadastrais completos de um aluno específico pelo seu ID.
                    
                    **Permissões requeridas:** Papéis `ALUNO`, `COORDENADOR`, `PROFESSOR` ou `ADMIN`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Aluno encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = alunoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado — usuário não possui permissão para consultar este recurso",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Aluno não encontrado para o ID informado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<alunoResponseDTO> buscarAlunoPorId(
            @Parameter(description = "ID numérico único do aluno", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(alunoService.buscarAlunoPorId(id));
    }

    @Operation(
            summary = "Buscar ou listar alunos",
            description = """
                    Permite listar alunos ou filtrá-los dinamicamente por nome e/ou ID do curso.
                    
                    - Se `nome` for informado, filtra alunos com correspondência parcial no nome.
                    - Se `cursoId` for informado (e nome ausente), filtra discentes matriculados naquele curso.
                    - Se nenhum parâmetro for informado, lista todos os alunos cadastrados.
                    
                    **Permissões requeridas:** Papéis `ALUNO`, `COORDENADOR`, `PROFESSOR` ou `ADMIN`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de alunos retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = alunoResponseDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado — usuário sem permissão necessária",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<alunoResponseDTO>> buscarAlunos(
            @Parameter(description = "Filtro parcial por nome do aluno (case-insensitive)", example = "Maria")
            @RequestParam(required = false) String nome,
            @Parameter(description = "ID do curso para filtrar alunos matriculados", example = "1")
            @RequestParam(required = false) Long cursoId
    ) {
        List<alunoResponseDTO> alunos;
        if (nome != null) {
            alunos = alunoService.buscarAlunosPorNome(nome);
        } else if (cursoId != null) {
            alunos = alunoService.buscarPorCurso(cursoId);
        } else {
            alunos = alunoService.listarAlunos();
        }
        return ResponseEntity.ok(alunos);
    }

    @Operation(
            summary = "Atualizar dados do aluno",
            description = """
                    Atualiza os dados de um aluno existente (dados do usuário e/ou vínculo de curso).
                    
                    **Permissões requeridas:** Papéis `ALUNO` ou `ADMIN`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Aluno atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = alunoResponseDTO.class))
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
                    description = "Acesso negado — usuário não tem papel ALUNO ou ADMIN",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Aluno não encontrado para atualização",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito — e-mail informado já pertence a outro usuário",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<alunoResponseDTO> atualizarAluno(
            @Parameter(description = "ID único do aluno a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody @Valid alunoRequestDTO request
    ) {
        return ResponseEntity.ok(alunoService.atualizarAluno(id, request));
    }

    @Operation(
            summary = "Remover aluno",
            description = """
                    Remove permanentemente um aluno do sistema pelo seu identificador único.
                    
                    **Permissões requeridas:** Apenas papel `ADMIN`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Aluno removido com sucesso (sem conteúdo no corpo)"
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
                    description = "Aluno não encontrado para exclusão",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(
            @Parameter(description = "ID único do aluno a ser removido", required = true, example = "1")
            @PathVariable Long id
    ) {
        alunoService.deletarAluno(id);
        return ResponseEntity.noContent().build();
    }
}
