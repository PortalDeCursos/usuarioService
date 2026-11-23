package PortalTADS.usuarioService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordenador {

    private Long id;
    private Professor professor;
    private Long cursoId;
}
