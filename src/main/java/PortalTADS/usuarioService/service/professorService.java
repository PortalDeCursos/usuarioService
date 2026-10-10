package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.model.dto.professor.professorRequestDTO;
import PortalTADS.usuarioService.model.dto.professor.professorResponseDTO;
import PortalTADS.usuarioService.model.mapper.professorMapper;
import PortalTADS.usuarioService.model.mapper.usuarioMapper;
import PortalTADS.usuarioService.model.professor;
import PortalTADS.usuarioService.model.usuario;
import PortalTADS.usuarioService.repository.entity.professorEntity;
import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import PortalTADS.usuarioService.repository.professorRepository;
import PortalTADS.usuarioService.repository.usuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class professorService {

    private final professorRepository professorRepository;
    private final usuarioRepository usuarioRepository;
    private final professorMapper professorMapper;
    private final usuarioMapper usuarioMapper;

    @Transactional
    public professorResponseDTO criarProfessor(professorRequestDTO professorRequestDTO) {
        professor professorModel = professorMapper.toModel(professorRequestDTO);
        usuario usuarioModel = professorModel.getUsuario();
        usuarioModel.setTipoUsuario(roleUsuario.PROFESSOR);
        usuarioEntity usuarioEntity = usuarioMapper.toEntity(usuarioModel);
        usuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);
        professorModel.setUsuario(usuarioMapper.toModel(usuarioSalvo));
        professorEntity professorEntity = professorMapper.toEntity(professorModel);
        professorEntity entidadeSalva = professorRepository.save(professorEntity);
        return professorMapper.toResponse(entidadeSalva);
    }

    public professorResponseDTO buscarProfessorPorId(Long id) {
        professorEntity entity = professorRepository.findById(id).orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        return professorMapper.toResponse(entity);
    }

    public List<professorResponseDTO> listarProfessores() {
        List<professorEntity> entities = professorRepository.findAll();
        return professorMapper.toResponseList(entities);
    }

    public List<professorResponseDTO> buscarProfessoresPorNome(String nome) {
        List<professorEntity> entities = professorRepository.buscarPorNomeUsuario(nome);
        return professorMapper.toResponseList(entities);
    }
}
