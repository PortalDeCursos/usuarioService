package PortalTADS.usuarioService.model.dto.usuario;

import PortalTADS.usuarioService.enums.roleUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados do usuário retornados nas respostas da API")
public class usuarioResponseDTO {

    @Schema(description = "Identificador único do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
    private String email;

    @Schema(description = "Senha do usuário (hash)", example = "$2a$10$...")
    private String senha;

    @Schema(description = "Nome de usuário", example = "joao.silva")
    private String username;

    @Schema(description = "Papel/perfil do usuário no sistema", example = "USER")
    private roleUsuario tipoUsuario;
}