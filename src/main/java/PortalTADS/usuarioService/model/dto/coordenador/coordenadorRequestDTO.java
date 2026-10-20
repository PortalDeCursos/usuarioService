package PortalTADS.usuarioService.model.dto.coordenador;

import PortalTADS.usuarioService.model.dto.usuario.usuarioRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class coordenadorRequestDTO {

    private usuarioRequestDTO usuario;
    private Long cursoId;
}
