package PortalTADS.usuarioService.model;

import PortalTADS.usuarioService.enums.TitulacaoProfessor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Professor {

    private Long id;
    private Usuario usuario;
    private Long cursoId;
    private TitulacaoProfessor titulacao;
    private String descricao;
}
