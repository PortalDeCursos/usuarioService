package PortalTADS.usuarioService.model.dto.aluno;

import PortalTADS.usuarioService.model.dto.usuario.UsuarioRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados necessários para criar ou atualizar um aluno")
public record AlunoRequestDTO(

       @NotNull(message = "Os dados do usuário são obrigatórios")
       @Valid
       @Schema(
               description = "Dados do usuário vinculado ao aluno",
               requiredMode = Schema.RequiredMode.REQUIRED
       )
       UsuarioRequestDTO usuario,

       @NotNull(message = "O ID do curso é obrigatório")
       @Positive(message = "O ID do curso deve ser positivo")
       @Schema(
               description = "ID do curso ao qual o aluno está vinculado",
               example = "1",
               requiredMode = Schema.RequiredMode.REQUIRED
       )
       Long cursoId
) {
}
