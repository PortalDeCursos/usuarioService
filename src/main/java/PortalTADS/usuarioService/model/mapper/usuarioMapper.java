package PortalTADS.usuarioService.model.mapper;

import PortalTADS.usuarioService.model.dto.usuario.usuarioRequestDTO;
import PortalTADS.usuarioService.model.dto.usuario.usuarioResponseDTO;
import PortalTADS.usuarioService.model.usuario;
import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface usuarioMapper {
    usuario toModel(usuarioRequestDTO record);

    usuarioEntity toEntity(usuario usuarioModel);

    usuarioResponseDTO toResponse(usuarioEntity entity);
}
