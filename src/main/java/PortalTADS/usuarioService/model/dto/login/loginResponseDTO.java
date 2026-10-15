package PortalTADS.usuarioService.model.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class loginResponseDTO {

    private String token;
    private long expiracao;
}
