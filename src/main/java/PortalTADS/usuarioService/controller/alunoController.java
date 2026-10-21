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
@Tag(name = "Alunos", description = "Gerenciamento de alunos vinculados a cursos")
public class alunoController {

    private final alunoService alunoService;

    @Operation(
            summary = "Cadastrar novo aluno",
            description = "Cria um novo aluno no sistema, associando-o a um usuário e a um curso."
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
                    responseCode = "401",
                    description = "Não autenticado — token JWT ausente ou inválido",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado — usuário não possui papel ADMIN",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito — e-mail do aluno já cadastrado no sistema",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<alunoResponseDTO> criarAluno(@RequestBody @Valid alunoRequestDTO request) {
        alunoResponseDTO response = alunoService.criarAluno(request);
        URI location = URI.create("/alunos/" + response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
    }

    @Operation(summary = "Buscar aluno por ID", description = "Retorna os dados completos de um aluno específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno encontrado", content = @Content(schema = @Schema(implementation = alunoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado — token JWT ausente ou inválido", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ALUNO', 'COORDENADOR', 'PROFESSOR', 'ADMIN')")
    public ResponseEntity<alunoResponseDTO> buscarAlunoPorId(
            @Parameter(description = "ID único do aluno", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(alunoService.buscarAlunoPorId(id));
    }

    @Operation(summary = "Buscar alunos", description = "Busca alunos por nome, por curso ou lista todos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = alunoResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado — token JWT ausente ou inválido", content = @Content)
    })
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ALUNO', 'COORDENADOR', 'PROFESSOR', 'ADMIN')")
    public ResponseEntity<List<alunoResponseDTO>> buscarAlunos(
            @Parameter(description = "Termo para buscar no nome do aluno", example = "Maria")
            @RequestParam(required = false) String nome,
            @Parameter(description = "ID do curso para filtrar alunos", example = "1")
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

    @Operation(summary = "Atualizar aluno", description = "Atualiza os dados de um aluno existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso", content = @Content(schema = @Schema(implementation = alunoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos — campos obrigatórios ausentes ou com formato incorreto", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado — token JWT ausente ou inválido", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ALUNO', 'ADMIN')")
    public ResponseEntity<alunoResponseDTO> atualizarAluno(
            @Parameter(description = "ID único do aluno a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody @Valid alunoRequestDTO request
    ) {
        return ResponseEntity.ok(alunoService.atualizarAluno(id, request));
    }

    @Operation(summary = "Deletar aluno", description = "Remove permanentemente um aluno do sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Aluno removido com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado — token JWT ausente ou inválido", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarAluno(
            @Parameter(description = "ID único do aluno a ser removido", required = true, example = "1")
            @PathVariable Long id
    ) {
        alunoService.deletarAluno(id);
        return ResponseEntity.noContent().build();
    }
}
