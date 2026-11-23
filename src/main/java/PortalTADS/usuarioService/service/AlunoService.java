package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.client.CursoClient;
import PortalTADS.usuarioService.enums.RoleUsuario;
import PortalTADS.usuarioService.exception.AcessoProibidoException;
import PortalTADS.usuarioService.exception.EmailDuplicadoException;
import PortalTADS.usuarioService.exception.UsuarioNaoEncontradoException;
import PortalTADS.usuarioService.model.Aluno;
import PortalTADS.usuarioService.model.Usuario;
import PortalTADS.usuarioService.model.dto.aluno.AlunoRequestDTO;
import PortalTADS.usuarioService.model.dto.aluno.AlunoResponseDTO;
import PortalTADS.usuarioService.model.mapper.AlunoMapper;
import PortalTADS.usuarioService.model.mapper.UsuarioMapper;
import PortalTADS.usuarioService.repository.AlunoRepository;
import PortalTADS.usuarioService.repository.entity.AlunoEntity;
import PortalTADS.usuarioService.repository.entity.UsuarioEntity;
import PortalTADS.usuarioService.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlunoMapper alunoMapper;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final CursoClient cursoClient;

    @Transactional
    public AlunoResponseDTO criarAluno(AlunoRequestDTO request) {
        Aluno alunoModel = alunoMapper.toModel(request);
        Usuario usuarioModel = alunoModel.getUsuario();

        if (usuarioRepository.existsByEmail(usuarioModel.getEmail())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }
        cursoClient.existeCurso(alunoModel.getCursoId());

        usuarioModel.setTipoUsuario(RoleUsuario.ALUNO);
        usuarioModel.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));

        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuarioModel);
        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);
        alunoModel.setUsuario(usuarioMapper.toModel(usuarioSalvo));

        AlunoEntity entity = alunoMapper.toEntity(alunoModel);
        AlunoEntity salvo = alunoRepository.save(entity);
        return alunoMapper.toResponse(salvo);
    }

    public AlunoResponseDTO buscarAlunoPorId(Long id) {
        AlunoEntity entity = alunoRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        return alunoMapper.toResponse(entity);
    }

    public List<AlunoResponseDTO> listarAlunos() {
        return alunoMapper.toResponseList(alunoRepository.findAll());
    }

    public List<AlunoResponseDTO> buscarAlunosPorNome(String nome) {
        return alunoMapper.toResponseList(alunoRepository.buscarPorNomeUsuario(nome));
    }

    public List<AlunoResponseDTO> buscarPorCurso(Long cursoId) {
        return alunoMapper.toResponseList(alunoRepository.findByCursoId(cursoId));
    }

    @Transactional
    public AlunoResponseDTO atualizarAluno(Long id, AlunoRequestDTO request) {
        AlunoEntity entity = alunoRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        UsuarioEntity usuarioEntity = entity.getUsuario();
        if (usuarioRepository.existsByEmailAndIdNot(request.usuario().email(), usuarioEntity.getId())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }

        cursoClient.existeCurso(request.cursoId());

        usuarioEntity.setNome(request.usuario().nome());
        usuarioEntity.setEmail(request.usuario().email());
        usuarioEntity.setSenha(passwordEncoder.encode(request.usuario().senha()));
        usuarioEntity.setTipoUsuario(RoleUsuario.ALUNO);

        UsuarioEntity usuarioAtualizado = usuarioRepository.save(usuarioEntity);

        entity.setUsuario(usuarioAtualizado);
        entity.setCursoId(request.cursoId());

        AlunoEntity atualizado = alunoRepository.save(entity);
        return alunoMapper.toResponse(atualizado);
    }

    @Transactional
    public AlunoResponseDTO atualizarPerfilProprio(String subEmail, AlunoRequestDTO request) {
        AlunoEntity entity = alunoRepository.findByUsuario_Email(subEmail)
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
        usuarioEntity.setTipoUsuario(RoleUsuario.ALUNO);

        usuarioRepository.save(usuarioEntity);

        entity.setUsuario(usuarioEntity);
        entity.setCursoId(request.cursoId());

        AlunoEntity atualizado = alunoRepository.save(entity);
        return alunoMapper.toResponse(atualizado);
    }

    @Transactional
    public void deletarAluno(Long id) {
        AlunoEntity entity = alunoRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        Long usuarioId = entity.getUsuario().getId();
        alunoRepository.delete(entity);
        usuarioRepository.deleteById(usuarioId);
    }
}
