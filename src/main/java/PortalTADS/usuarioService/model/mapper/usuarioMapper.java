package PortalTADS.usuarioService.model.mapper;

import PortalTADS.usuarioService.model.dto.usuario.usuarioRequestDTO;
import PortalTADS.usuarioService.model.dto.usuario.usuarioResponseDTO;
import PortalTADS.usuarioService.model.usuario;
import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface usuarioMapper {
    usuario toModel(usuarioRequestDTO record);
    usuario toModel(usuarioEntity entity);

    usuarioEntity toEntity(usuario usuarioModel);

    usuarioResponseDTO toResponse(usuarioEntity entity);

    List<usuarioResponseDTO> toResponseList(List<usuarioEntity> enitdades);
}
