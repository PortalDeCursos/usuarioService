package PortalTADS.usuarioService.model.dto.usuario;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados necessários para criar ou atualizar um usuário")
public record usuarioRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        @Schema(
                description = "Nome completo do usuário",
                example = "João da Silva",
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Informe um e-mail válido")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
        @Schema(
                description = "Endereço de e-mail do usuário (deve ser único no sistema)",
                example = "joao.silva@email.com",
                maxLength = 150,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, max = 100,
                message = "A senha deve ter entre 8 e 100 caracteres")
        @Schema(
                description = "Senha do usuário (mínimo 8 caracteres)",
                example = "senhaSegura123",
                minLength = 8,
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String senha

) {
}
