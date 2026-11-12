package PortalTADS.usuarioService.service;


import PortalTADS.usuarioService.client.cursoClient;
import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.enums.titulacaoProfessor;
import PortalTADS.usuarioService.exception.AcessoProibidoException;
import PortalTADS.usuarioService.exception.EmailDuplicadoException;
import PortalTADS.usuarioService.exception.UsuarioNaoEncontradoException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class professorService {

    private final professorRepository professorRepository;
    private final usuarioRepository usuarioRepository;
    private final professorMapper professorMapper;
    private final usuarioMapper usuarioMapper;

    private final PasswordEncoder passwordEncoder;
    private final cursoClient cursoClient;

    @Transactional
    public professorResponseDTO criarProfessor(professorRequestDTO professorRequestDTO) {
        professor professorModel = professorMapper.toModel(professorRequestDTO);
        usuario usuarioModel = professorModel.getUsuario();

        if (usuarioRepository.existsByEmail(usuarioModel.getEmail())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }
        if (!cursoClient.existeCurso(professorRequestDTO.cursoId())) {
            throw new IllegalArgumentException("Curso não encontrado.");
        }

        usuarioModel.setTipoUsuario(roleUsuario.PROFESSOR);
        usuarioModel.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));
        usuarioEntity usuarioEntity = usuarioMapper.toEntity(usuarioModel);
        usuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);
        professorModel.setUsuario(usuarioMapper.toModel(usuarioSalvo));
        professorEntity professorEntity = professorMapper.toEntity(professorModel);
        professorEntity entidadeSalva = professorRepository.save(professorEntity);
        return professorMapper.toResponse(entidadeSalva);
    }

    public professorResponseDTO buscarProfessorPorId(Long id) {
        professorEntity entity = professorRepository.findById(id).orElseThrow(() ->
                new UsuarioNaoEncontradoException(id));
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
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        usuarioEntity usuarioEntity = entity.getUsuario();
        if (usuarioRepository.existsByEmailAndIdNot(professorRequestDTO.usuario().email(), usuarioEntity.getId())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }
        if (!cursoClient.existeCurso(professorRequestDTO.cursoId())) {
            throw new IllegalArgumentException("Curso não encontrado.");
        }

        usuarioEntity.setNome(professorRequestDTO.usuario().nome());
        usuarioEntity.setEmail(professorRequestDTO.usuario().email());
        usuarioEntity.setSenha(passwordEncoder.encode(professorRequestDTO.usuario().senha()));
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
    public professorResponseDTO atualizarPerfilProprio(String subEmail, professorRequestDTO request) {
        professorEntity entity = professorRepository.findByUsuario_Email(subEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(subEmail));

        usuarioEntity usuarioEntity = entity.getUsuario();

        if (!usuarioEntity.getEmail().equals(subEmail)) {
            throw new AcessoProibidoException("Você não tem permissão para atualizar este perfil.");
        }

        if (usuarioRepository.existsByEmailAndIdNot(request.usuario().email(), usuarioEntity.getId())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }
        if (!cursoClient.existeCurso(request.cursoId())) {
            throw new IllegalArgumentException("Curso não encontrado.");
        }

        usuarioEntity.setNome(request.usuario().nome());
        usuarioEntity.setEmail(request.usuario().email());
        usuarioEntity.setSenha(passwordEncoder.encode(request.usuario().senha()));
        usuarioEntity.setTipoUsuario(roleUsuario.PROFESSOR);

        usuarioRepository.save(usuarioEntity);

        entity.setUsuario(usuarioEntity);
        entity.setCursoId(request.cursoId());
        entity.setTitulacao(request.titulacao());
        entity.setDescricao(request.descricao());

        professorEntity atualizado = professorRepository.save(entity);
        return professorMapper.toResponse(atualizado);
    }

    @Transactional
    public void deletarProfessor(Long id) {
        professorEntity entity = professorRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        Long usuarioId = entity.getUsuario().getId();
        professorRepository.delete(entity);
        usuarioRepository.deleteById(usuarioId);
    }
}
