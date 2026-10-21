package PortalTADS.usuarioService.model.mapper;

import PortalTADS.usuarioService.model.coordenador;
import PortalTADS.usuarioService.model.dto.coordenador.coordenadorRequestDTO;
import PortalTADS.usuarioService.model.dto.coordenador.coordenadorResponseDTO;
import PortalTADS.usuarioService.repository.entity.coordenadorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface coordenadorMapper {

    @Mapping(target = "professor.id", source = "professorId")
    coordenador toModel(coordenadorRequestDTO record);

    coordenadorEntity toEntity(coordenador coordenadorModel);

    coordenadorResponseDTO toResponse(coordenadorEntity entity);

    List<coordenadorResponseDTO> toResponseList(List<coordenadorEntity> entities);
}
