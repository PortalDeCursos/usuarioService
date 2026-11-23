package PortalTADS.usuarioService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class foto {
    private Long id;
    private Long usuarioId;
    private String nomeArquivo;

}
