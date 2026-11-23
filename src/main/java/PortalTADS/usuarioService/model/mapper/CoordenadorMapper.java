package PortalTADS.usuarioService.model.mapper;

import PortalTADS.usuarioService.model.Coordenador;
import PortalTADS.usuarioService.model.dto.coordenador.CoordenadorRequestDTO;
import PortalTADS.usuarioService.model.dto.coordenador.CoordenadorResponseDTO;
import PortalTADS.usuarioService.repository.entity.CoordenadorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoordenadorMapper {

    @Mapping(target = "professor.id", source = "professorId")
    Coordenador toModel(CoordenadorRequestDTO record);

    CoordenadorEntity toEntity(Coordenador coordenadorModel);

    CoordenadorResponseDTO toResponse(CoordenadorEntity entity);

    List<CoordenadorResponseDTO> toResponseList(List<CoordenadorEntity> entities);
}
