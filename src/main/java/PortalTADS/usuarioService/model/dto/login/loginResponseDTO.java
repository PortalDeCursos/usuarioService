package PortalTADS.usuarioService.model.dto.login;

import PortalTADS.usuarioService.enums.roleUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Resposta de autenticação contendo o token JWT e seu tempo de expiração")
public class loginResponseDTO {

    @Schema(
            description = "Token JWT para autenticação nas demais requisições",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String token;

    @Schema(
            description = "Tempo de expiração do token em milissegundos",
            example = "3600000"
    )
    private long expiracao;

    @Schema(
            description = "Role do usuário autenticado",
            example = "ADMIN"
    )
    private String role;
}
