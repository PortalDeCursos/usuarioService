package PortalTADS.usuarioService.model.mapper;

import PortalTADS.usuarioService.model.dto.usuario.UsuarioRequestDTO;
import PortalTADS.usuarioService.model.dto.usuario.UsuarioResponseDTO;
import PortalTADS.usuarioService.model.Usuario;
import PortalTADS.usuarioService.repository.entity.UsuarioEntity;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    Usuario toModel(UsuarioRequestDTO record);
    Usuario toModel(UsuarioEntity entity);

    UsuarioEntity toEntity(Usuario usuarioModel);

    UsuarioResponseDTO toResponse(UsuarioEntity entity);

    List<UsuarioResponseDTO> toResponseList(List<UsuarioEntity> enitdades);
}
