package PortalTADS.usuarioService.model.dto.foto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados de retorno da foto de perfil cadastrada ou atualizada")
public class FotoResponseDTO {

    @Schema(description = "Identificador único do registro da foto", example = "1")
    private Long id;

    @Schema(description = "Identificador único do usuário proprietário da foto", example = "1")
    private Long usuarioId;

    @Schema(description = "Nome do arquivo gerado e armazenado no servidor", example = "foto_1_abc123.png")
    private String nomeArquivo;

}
