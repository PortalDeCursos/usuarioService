package PortalTADS.usuarioService.model.dto.coordenador;

import PortalTADS.usuarioService.model.dto.usuario.usuarioResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class coordenadorResponseDTO {
    private Long id;
    private usuarioResponseDTO usuario;
    private Long cursoId;
}
