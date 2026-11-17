package PortalTADS.usuarioService.model.dto.curso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "DTO para receber dados essenciais do curso a partir do msCurso")
public class cursoResponseDTO {

    @Schema(description = "Identificador único do curso", example = "2")
    private Long id;
}
