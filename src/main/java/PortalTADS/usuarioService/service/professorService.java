package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.model.dto.professor.professorRequestDTO;
import PortalTADS.usuarioService.model.dto.professor.professorResponseDTO;
import PortalTADS.usuarioService.model.mapper.professorMapper;
import PortalTADS.usuarioService.model.professor;
import PortalTADS.usuarioService.model.usuario;
import PortalTADS.usuarioService.repository.professorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class professorService {

    private final professorRepository professorRepository;
    private final professorMapper professorMapper;


    public professorResponseDTO criarProfessor(professorRequestDTO professorRequestDTO) {
        professor professorModel = professorMapper.toModel(professorRequestDTO);

    }

}
