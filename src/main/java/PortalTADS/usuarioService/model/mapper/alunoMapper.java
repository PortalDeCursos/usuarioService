package PortalTADS.usuarioService.model.mapper;

import PortalTADS.usuarioService.model.aluno;
import PortalTADS.usuarioService.model.dto.aluno.alunoRequestDTO;
import PortalTADS.usuarioService.model.dto.aluno.alunoResponseDTO;
import PortalTADS.usuarioService.repository.entity.alunoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface alunoMapper {

    aluno toModel(alunoRequestDTO record);

    alunoEntity toEntity(aluno alunoModel);

    alunoResponseDTO toResponse(alunoEntity entity);

    List<alunoResponseDTO> toResponseList(List<alunoEntity> entities);
}
