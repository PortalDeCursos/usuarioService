package PortalTADS.usuarioService.model;

import PortalTADS.usuarioService.enums.titulacaoProfessor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class professor {

    private Long id;
    private Long usuarioId;
    private Long cursoId;
    private titulacaoProfessor titulacao;
    private String descricao;
}
