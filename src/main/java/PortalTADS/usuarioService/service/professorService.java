package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.enums.titulacaoProfessor;
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

    public List<professorResponseDTO> buscarPorTitulacao(titulacaoProfessor titulacao) {
        List<professorEntity> entitidades = professorRepository.findByTitulacao(titulacao);
        return professorMapper.toResponseList(entitidades);
    }

    @Transactional
    public professorResponseDTO atualizarProfessor(Long id, professorRequestDTO professorRequestDTO) {
        professorEntity entity = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        usuarioEntity usuarioEntity = entity.getUsuario();
        usuarioEntity.setNome(professorRequestDTO.usuario().nome());
        usuarioEntity.setEmail(professorRequestDTO.usuario().email());
        usuarioEntity.setUsername(professorRequestDTO.usuario().username());
        usuarioEntity.setSenha(professorRequestDTO.usuario().senha());
        usuarioEntity.setTipoUsuario(roleUsuario.PROFESSOR);

        usuarioEntity usuarioAtualizado = usuarioRepository.save(usuarioEntity);

        entity.setUsuario(usuarioAtualizado);
        entity.setCursoId(professorRequestDTO.cursoId());
        entity.setTitulacao(professorRequestDTO.titulacao());
        entity.setDescricao(professorRequestDTO.descricao());

        professorEntity professorAtualizado = professorRepository.save(entity);
        return professorMapper.toResponse(professorAtualizado);
    }

    @Transactional
    public void deletarProfessor(Long id) {
        professorEntity entity = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        Long usuarioId = entity.getUsuario().getId();
        professorRepository.delete(entity);
        usuarioRepository.deleteById(usuarioId);
    }
}
