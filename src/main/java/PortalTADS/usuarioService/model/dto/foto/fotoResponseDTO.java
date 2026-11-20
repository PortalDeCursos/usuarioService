package PortalTADS.usuarioService.model.dto.foto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class fotoResponseDTO {
    private Long id;
    private Long usuarioId;
    private String caminhoArquivo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
