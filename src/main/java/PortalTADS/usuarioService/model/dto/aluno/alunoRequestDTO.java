package PortalTADS.usuarioService.model.dto.aluno;

import PortalTADS.usuarioService.model.dto.usuario.usuarioRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class alunoRequestDTO {

   private usuarioRequestDTO usuario;
   private Long cursoId;
}
