package PortalTADS.usuarioService.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class aluno {

    private Long id;
    private usuario usuario;
    private Long cursoId;
}
