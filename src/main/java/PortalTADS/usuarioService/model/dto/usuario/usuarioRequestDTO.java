package PortalTADS.usuarioService.model.dto.usuario;


public record usuarioRequestDTO(
        String nome,
        String email,
        String senha,
        String username
) {
}
