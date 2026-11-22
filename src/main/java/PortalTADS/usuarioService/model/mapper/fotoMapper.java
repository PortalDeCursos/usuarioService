package PortalTADS.usuarioService.model.mapper;

import PortalTADS.usuarioService.model.dto.foto.fotoRequestDTO;
import PortalTADS.usuarioService.model.dto.foto.fotoResponseDTO;
import PortalTADS.usuarioService.model.foto;
import PortalTADS.usuarioService.repository.entity.fotoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface fotoMapper {
    foto toModel(fotoRequestDTO request);

    fotoEntity toEntity(foto fotoModel);

    fotoResponseDTO toResponseDTO(fotoEntity entity);
}
