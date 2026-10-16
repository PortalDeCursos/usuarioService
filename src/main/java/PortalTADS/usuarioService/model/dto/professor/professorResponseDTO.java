package PortalTADS.usuarioService.model.dto.professor;

import PortalTADS.usuarioService.enums.titulacaoProfessor;
import PortalTADS.usuarioService.model.dto.usuario.usuarioRequestDTO;
import PortalTADS.usuarioService.model.dto.usuario.usuarioResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados do professor retornados nas respostas da API")
public class professorResponseDTO {

    @Schema(description = "Identificador único do professor", example = "1")
    private Long id;

    @Schema(description = "Dados do usuário vinculado ao professor")
    private usuarioResponseDTO usuario;

    @Schema(description = "ID do curso ao qual o professor está vinculado", example = "1")
    private Long cursoId;

    @Schema(description = "Titulação acadêmica do professor", example = "DOUTORADO")
    private titulacaoProfessor titulacao;

    @Schema(description = "Descrição sobre o professor e sua atuação", example = "Professor de Engenharia de Software com foco em arquitetura de microsserviços")
    private String descricao;
}
