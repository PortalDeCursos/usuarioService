package PortalTADS.usuarioService.model.dto.professor;

import PortalTADS.usuarioService.enums.titulacaoProfessor;
import PortalTADS.usuarioService.model.dto.usuario.usuarioRequestDTO;

public record professorRequestDTO(
        usuarioRequestDTO usuario,
        Long cursoId,
        titulacaoProfessor titulacao,
        String descricao
) {
}
