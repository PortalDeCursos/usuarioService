package PortalTADS.usuarioService.model.dto.aluno;

import PortalTADS.usuarioService.model.dto.usuario.usuarioResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados do aluno retornados nas respostas da API")
public class alunoResponseDTO {

    @Schema(description = "Identificador único do aluno", example = "1")
    private Long id;

    @Schema(description = "Dados do usuário vinculado ao aluno")
    private usuarioResponseDTO usuario;

    @Schema(description = "ID do curso ao qual o aluno está vinculado", example = "1")
    private Long cursoId;
}
