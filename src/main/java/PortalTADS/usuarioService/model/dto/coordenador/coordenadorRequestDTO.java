package PortalTADS.usuarioService.model.dto.coordenador;

import PortalTADS.usuarioService.model.dto.usuario.usuarioRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados necessários para criar ou atualizar um coordenador")
public record coordenadorRequestDTO(

        @NotNull(message = "Os dados do usuário são obrigatórios")
        @Valid
        @Schema(
                description = "Dados do usuário vinculado ao coordenador",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        usuarioRequestDTO usuario,

        @NotNull(message = "O ID do curso é obrigatório")
        @Positive(message = "O ID do curso deve ser positivo")
        @Schema(
                description = "ID do curso ao qual o coordenador está vinculado",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long cursoId
) {
}
