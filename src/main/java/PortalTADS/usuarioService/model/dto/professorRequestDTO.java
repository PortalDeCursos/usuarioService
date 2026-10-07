package PortalTADS.usuarioService.model.dto;

import PortalTADS.usuarioService.enums.titulacaoProfessor;

public record professorRequestDTO(
        Long usuarioId,
        Long cursoId,
        titulacaoProfessor titulacao,
        String descricao
) {
}
