package PortalTADS.usuarioService.model.dto.aluno;

import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.model.dto.usuario.usuarioResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;



public class alunoResponseDTO {

    private Long id;
    private usuarioResponseDTO usuario;
    private Long cursoId;
}
