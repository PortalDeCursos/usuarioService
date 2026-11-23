package PortalTADS.usuarioService.model.mapper;

import PortalTADS.usuarioService.model.dto.professor.ProfessorRequestDTO;
import PortalTADS.usuarioService.model.dto.professor.ProfessorResponseDTO;
import PortalTADS.usuarioService.model.Professor;
import PortalTADS.usuarioService.repository.entity.ProfessorEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {

    Professor toModel(ProfessorRequestDTO record);

    ProfessorEntity toEntity(Professor professorModel);

    ProfessorResponseDTO toResponse(ProfessorEntity entity);

    List<ProfessorResponseDTO> toResponseList(List<ProfessorEntity> entities);
}
