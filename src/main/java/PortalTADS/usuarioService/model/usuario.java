package PortalTADS.usuarioService.model;

import PortalTADS.usuarioService.enums.roleUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class usuario {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String username;
    private roleUsuario tipoUsuario;
}
