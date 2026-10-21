package PortalTADS.usuarioService.model.dto.coordenador;

import PortalTADS.usuarioService.model.dto.usuario.usuarioResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados do coordenador retornados nas respostas da API")
public class coordenadorResponseDTO {

    @Schema(description = "Identificador único do coordenador", example = "1")
    private Long id;

    @Schema(description = "Dados do usuário vinculado ao coordenador")
    private usuarioResponseDTO usuario;

    @Schema(description = "ID do curso ao qual o coordenador está vinculado", example = "1")
    private Long cursoId;
}
