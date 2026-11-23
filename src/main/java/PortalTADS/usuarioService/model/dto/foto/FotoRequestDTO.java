package PortalTADS.usuarioService.model.dto.foto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados para envio e processamento da foto de perfil")
public class FotoRequestDTO {

    @NotNull(message = "O id do usuário é obrigatório")
    @Schema(
            description = "Identificador único do usuário associado à foto",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long usuarioId;

    @NotNull(message = "A foto é obrigatória")
    @Schema(
            description = "Arquivo da imagem da foto (formatos aceitos: PNG, JPEG, etc., tamanho máximo de 2 MB)",
            type = "string",
            format = "binary",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private MultipartFile foto;

}
