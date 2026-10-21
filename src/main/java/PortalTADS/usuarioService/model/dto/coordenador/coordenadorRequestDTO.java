package PortalTADS.usuarioService.model.dto.coordenador;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados necessários para criar ou atualizar um coordenador")
public record coordenadorRequestDTO(

        @NotNull(message = "O ID do professor é obrigatório")
        @Schema(
                description = "ID de um professor já cadastrado no sistema",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long professorId,

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
