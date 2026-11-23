package PortalTADS.usuarioService.model.dto.professor;

import PortalTADS.usuarioService.enums.TitulacaoProfessor;
import PortalTADS.usuarioService.model.dto.usuario.UsuarioRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados necessários para criar ou atualizar um professor")
public record ProfessorRequestDTO(

        @NotNull(message = "Os dados do usuário são obrigatórios")
        @Valid
        @Schema(
                description = "Dados do usuário vinculado ao professor",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UsuarioRequestDTO usuario,

        @NotNull(message = "O ID do curso é obrigatório")
        @Positive(message = "O ID do curso deve ser positivo")
        @Schema(
                description = "ID do curso ao qual o professor está vinculado",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long cursoId,

        @NotNull(message = "A titulação do professor é obrigatória")
        @Schema(
                description = "Titulação acadêmica do professor",
                example = "DOUTORADO",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        TitulacaoProfessor titulacao,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        @Schema(
                description = "Breve descrição sobre o professor e sua atuação",
                example = "Professor de Engenharia de Software com foco em arquitetura de microsserviços",
                maxLength = 500,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String descricao

) {
}
