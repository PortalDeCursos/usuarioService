package PortalTADS.usuarioService.model.mapper;

import PortalTADS.usuarioService.model.dto.foto.FotoRequestDTO;
import PortalTADS.usuarioService.model.dto.foto.FotoResponseDTO;
import PortalTADS.usuarioService.model.Foto;
import PortalTADS.usuarioService.repository.entity.FotoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FotoMapper {
    Foto toModel(FotoRequestDTO request);

    @Mapping(target = "usuario.id", source = "usuarioId")
    FotoEntity toEntity(Foto fotoModel);

    @Mapping(target = "usuarioId", source = "usuario.id")
    FotoResponseDTO toResponseDTO(FotoEntity entity);
}
