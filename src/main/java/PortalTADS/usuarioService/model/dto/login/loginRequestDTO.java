package PortalTADS.usuarioService.model.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados necessários para realizar o login no sistema")
public class loginRequestDTO {

    @NotBlank
    @Email
    @Schema(
            description = "E-mail do usuário cadastrado",
            example = "usuario@email.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @NotBlank
    @Schema(
            description = "Senha do usuário",
            example = "minhasenha123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String senha;
}
