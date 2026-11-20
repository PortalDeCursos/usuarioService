package PortalTADS.usuarioService.model.dto.foto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class fotoRequestDTO  {
    private Long usuarioId;
    private MultipartFile foto;

}
