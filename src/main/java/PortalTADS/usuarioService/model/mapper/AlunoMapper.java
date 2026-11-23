package PortalTADS.usuarioService.model.mapper;

import PortalTADS.usuarioService.model.Aluno;
import PortalTADS.usuarioService.model.dto.aluno.AlunoRequestDTO;
import PortalTADS.usuarioService.model.dto.aluno.AlunoResponseDTO;
import PortalTADS.usuarioService.repository.entity.AlunoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlunoMapper {

    Aluno toModel(AlunoRequestDTO record);

    AlunoEntity toEntity(Aluno alunoModel);

    AlunoResponseDTO toResponse(AlunoEntity entity);

    List<AlunoResponseDTO> toResponseList(List<AlunoEntity> entities);
}
