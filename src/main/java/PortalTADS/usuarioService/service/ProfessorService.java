package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.client.CursoClient;
import PortalTADS.usuarioService.enums.RoleUsuario;
import PortalTADS.usuarioService.enums.TitulacaoProfessor;
import PortalTADS.usuarioService.exception.AcessoProibidoException;
import PortalTADS.usuarioService.exception.EmailDuplicadoException;
import PortalTADS.usuarioService.exception.UsuarioNaoEncontradoException;
import PortalTADS.usuarioService.model.Professor;
import PortalTADS.usuarioService.model.Usuario;
import PortalTADS.usuarioService.model.dto.professor.ProfessorRequestDTO;
import PortalTADS.usuarioService.model.dto.professor.ProfessorResponseDTO;
import PortalTADS.usuarioService.model.mapper.ProfessorMapper;
import PortalTADS.usuarioService.model.mapper.UsuarioMapper;
import PortalTADS.usuarioService.repository.entity.ProfessorEntity;
import PortalTADS.usuarioService.repository.entity.UsuarioEntity;
import PortalTADS.usuarioService.repository.ProfessorRepository;
import PortalTADS.usuarioService.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProfessorMapper professorMapper;
    private final UsuarioMapper usuarioMapper;

    private final PasswordEncoder passwordEncoder;
    private final CursoClient cursoClient;

    @Transactional
    public ProfessorResponseDTO criarProfessor(ProfessorRequestDTO professorRequestDTO) {
        Professor professorModel = professorMapper.toModel(professorRequestDTO);
        Usuario usuarioModel = professorModel.getUsuario();

        if (usuarioRepository.existsByEmail(usuarioModel.getEmail())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }
        cursoClient.existeCurso(professorRequestDTO.cursoId());

        usuarioModel.setTipoUsuario(RoleUsuario.PROFESSOR);
        usuarioModel.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));
        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuarioModel);
        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);
        professorModel.setUsuario(usuarioMapper.toModel(usuarioSalvo));
        ProfessorEntity professorEntity = professorMapper.toEntity(professorModel);
        ProfessorEntity entidadeSalva = professorRepository.save(professorEntity);
        return professorMapper.toResponse(entidadeSalva);
    }

    public ProfessorResponseDTO buscarProfessorPorId(Long id) {
        ProfessorEntity entity = professorRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        return professorMapper.toResponse(entity);
    }

    public List<ProfessorResponseDTO> listarProfessores() {
        List<ProfessorEntity> entities = professorRepository.findAll();
        return professorMapper.toResponseList(entities);
    }

    public List<ProfessorResponseDTO> buscarProfessoresPorNome(String nome) {
        List<ProfessorEntity> entities = professorRepository.buscarPorNomeUsuario(nome);
        return professorMapper.toResponseList(entities);
    }

    public List<ProfessorResponseDTO> buscarPorTitulacao(TitulacaoProfessor titulacao) {
        List<ProfessorEntity> entitidades = professorRepository.findByTitulacao(titulacao);
        return professorMapper.toResponseList(entitidades);
    }

    @Transactional
    public ProfessorResponseDTO atualizarProfessor(Long id, ProfessorRequestDTO professorRequestDTO) {
        ProfessorEntity entity = professorRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        UsuarioEntity usuarioEntity = entity.getUsuario();
        if (usuarioRepository.existsByEmailAndIdNot(professorRequestDTO.usuario().email(), usuarioEntity.getId())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }
        cursoClient.existeCurso(professorRequestDTO.cursoId());

        usuarioEntity.setNome(professorRequestDTO.usuario().nome());
        usuarioEntity.setEmail(professorRequestDTO.usuario().email());
        usuarioEntity.setSenha(passwordEncoder.encode(professorRequestDTO.usuario().senha()));
        usuarioEntity.setTipoUsuario(RoleUsuario.PROFESSOR);

        UsuarioEntity usuarioAtualizado = usuarioRepository.save(usuarioEntity);

        entity.setUsuario(usuarioAtualizado);
        entity.setCursoId(professorRequestDTO.cursoId());
        entity.setTitulacao(professorRequestDTO.titulacao());
        entity.setDescricao(professorRequestDTO.descricao());

        ProfessorEntity professorAtualizado = professorRepository.save(entity);
        return professorMapper.toResponse(professorAtualizado);
    }

    @Transactional
    public ProfessorResponseDTO atualizarPerfilProprio(String subEmail, ProfessorRequestDTO request) {
        ProfessorEntity entity = professorRepository.findByUsuario_Email(subEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(subEmail));

        UsuarioEntity usuarioEntity = entity.getUsuario();

        if (!usuarioEntity.getEmail().equals(subEmail)) {
            throw new AcessoProibidoException("Você não tem permissão para atualizar este perfil.");
        }

        if (usuarioRepository.existsByEmailAndIdNot(request.usuario().email(), usuarioEntity.getId())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }
        cursoClient.existeCurso(request.cursoId());

        usuarioEntity.setNome(request.usuario().nome());
        usuarioEntity.setEmail(request.usuario().email());
        usuarioEntity.setSenha(passwordEncoder.encode(request.usuario().senha()));
        usuarioEntity.setTipoUsuario(RoleUsuario.PROFESSOR);

        usuarioRepository.save(usuarioEntity);

        entity.setUsuario(usuarioEntity);
        entity.setCursoId(request.cursoId());
        entity.setTitulacao(request.titulacao());
        entity.setDescricao(request.descricao());

        ProfessorEntity atualizado = professorRepository.save(entity);
        return professorMapper.toResponse(atualizado);
    }

    @Transactional
    public void deletarProfessor(Long id) {
        ProfessorEntity entity = professorRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        Long usuarioId = entity.getUsuario().getId();
        professorRepository.delete(entity);
        usuarioRepository.deleteById(usuarioId);
    }
}
