package PortalTADS.usuarioService.model.dto;

import PortalTADS.usuarioService.enums.roleUsuario;



public record usuarioRequestDTO(
        String nome,
        String email,
        String senha,
        String matricula,
        roleUsuario role
) {
}
