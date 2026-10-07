package PortalTADS.usuarioService.model.mapper;

import PortalTADS.usuarioService.model.dto.professor.professorRequestDTO;
import PortalTADS.usuarioService.model.dto.professor.professorResponseDTO;
import PortalTADS.usuarioService.model.professor;
import PortalTADS.usuarioService.repository.entity.professorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface professorMapper {

    professor toModel(professorRequestDTO record);

    professorEntity toEntity(professor professorModel);

    professorResponseDTO toResponse(professorEntity entity);
}
