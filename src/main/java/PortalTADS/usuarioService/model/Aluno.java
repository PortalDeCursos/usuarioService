package PortalTADS.usuarioService.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aluno {

    private Long id;
    private Usuario usuario;
    private Long cursoId;
}
