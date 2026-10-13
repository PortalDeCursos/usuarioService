package PortalTADS.usuarioService.model.dto.professor;

import PortalTADS.usuarioService.enums.titulacaoProfessor;
import PortalTADS.usuarioService.model.dto.usuario.usuarioRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record professorRequestDTO(
        @NotNull(message = "Os dados do usuário são obrigatórios")
        @Valid
        usuarioRequestDTO usuario,

        @NotNull(message = "O ID do curso é obrigatório")
        @Positive(message = "O ID do curso deve ser positivo")
        Long cursoId,

        @NotNull(message = "A titulação do professor é obrigatória")
        titulacaoProfessor titulacao,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String descricao

) {
}
