package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.exception.EmailDuplicadoException;
import PortalTADS.usuarioService.exception.UsuarioNaoEncontradoException;
import PortalTADS.usuarioService.model.aluno;
import PortalTADS.usuarioService.model.dto.aluno.alunoRequestDTO;
import PortalTADS.usuarioService.model.dto.aluno.alunoResponseDTO;
import PortalTADS.usuarioService.model.mapper.alunoMapper;
import PortalTADS.usuarioService.model.mapper.usuarioMapper;
import PortalTADS.usuarioService.model.usuario;
import PortalTADS.usuarioService.repository.alunoRepository;
import PortalTADS.usuarioService.repository.entity.alunoEntity;
import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import PortalTADS.usuarioService.repository.usuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class alunoService {

    private final alunoRepository alunoRepository;
    private final usuarioRepository usuarioRepository;
    private final alunoMapper alunoMapper;
    private final usuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public alunoResponseDTO criarAluno(alunoRequestDTO request) {
        aluno alunoModel = alunoMapper.toModel(request);
        usuario usuarioModel = alunoModel.getUsuario();

        if (usuarioRepository.existsByEmail(usuarioModel.getEmail())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }

        usuarioModel.setTipoUsuario(roleUsuario.ALUNO);
        usuarioModel.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));

        usuarioEntity usuarioEntity = usuarioMapper.toEntity(usuarioModel);
        usuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);
        alunoModel.setUsuario(usuarioMapper.toModel(usuarioSalvo));

        alunoEntity entity = alunoMapper.toEntity(alunoModel);
        alunoEntity salvo = alunoRepository.save(entity);
        return alunoMapper.toResponse(salvo);
    }

    public alunoResponseDTO buscarAlunoPorId(Long id) {
        alunoEntity entity = alunoRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        return alunoMapper.toResponse(entity);
    }

    public List<alunoResponseDTO> listarAlunos() {
        return alunoMapper.toResponseList(alunoRepository.findAll());
    }

    public List<alunoResponseDTO> buscarAlunosPorNome(String nome) {
        return alunoMapper.toResponseList(alunoRepository.buscarPorNomeUsuario(nome));
    }

    public List<alunoResponseDTO> buscarPorCurso(Long cursoId) {
        return alunoMapper.toResponseList(alunoRepository.findByCursoId(cursoId));
    }

    @Transactional
    public alunoResponseDTO atualizarAluno(Long id, alunoRequestDTO request) {
        alunoEntity entity = alunoRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        usuarioEntity usuarioEntity = entity.getUsuario();
        if (usuarioRepository.existsByEmailAndIdNot(request.usuario().email(), usuarioEntity.getId())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }

        usuarioEntity.setNome(request.usuario().nome());
        usuarioEntity.setEmail(request.usuario().email());
        usuarioEntity.setSenha(passwordEncoder.encode(request.usuario().senha()));
        usuarioEntity.setTipoUsuario(roleUsuario.ALUNO);

        usuarioEntity usuarioAtualizado = usuarioRepository.save(usuarioEntity);

        entity.setUsuario(usuarioAtualizado);
        entity.setCursoId(request.cursoId());

        alunoEntity atualizado = alunoRepository.save(entity);
        return alunoMapper.toResponse(atualizado);
    }

    @Transactional
    public void deletarAluno(Long id) {
        alunoEntity entity = alunoRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        Long usuarioId = entity.getUsuario().getId();
        alunoRepository.delete(entity);
        usuarioRepository.deleteById(usuarioId);
    }
}
