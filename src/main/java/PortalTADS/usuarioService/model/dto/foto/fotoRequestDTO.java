package PortalTADS.usuarioService.model.dto.foto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class fotoRequestDTO  {

    @NotNull(message = "O id do usuário é obrigatório")
    private Long usuarioId;
    @NotNull(message = "A foto é obrigatória")
    private MultipartFile foto;

}
