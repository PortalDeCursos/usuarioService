package PortalTADS.usuarioService.model.dto.professor;

import PortalTADS.usuarioService.enums.titulacaoProfessor;
import PortalTADS.usuarioService.model.dto.usuario.usuarioRequestDTO;
import PortalTADS.usuarioService.model.dto.usuario.usuarioResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class professorResponseDTO {

    private Long id;
    private usuarioResponseDTO usuario;
    private Long cursoId;
    private titulacaoProfessor titulacao;
    private String descricao;
}
