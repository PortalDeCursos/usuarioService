package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.exception.EmailDuplicadoException;
import PortalTADS.usuarioService.model.coordenador;
import PortalTADS.usuarioService.model.dto.coordenador.coordenadorRequestDTO;
import PortalTADS.usuarioService.model.dto.coordenador.coordenadorResponseDTO;
import PortalTADS.usuarioService.model.mapper.coordenadorMapper;
import PortalTADS.usuarioService.model.mapper.usuarioMapper;
import PortalTADS.usuarioService.model.usuario;
import PortalTADS.usuarioService.repository.coordenadorRepository;
import PortalTADS.usuarioService.repository.entity.coordenadorEntity;
import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import PortalTADS.usuarioService.repository.usuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class coordenadorService {

    private final coordenadorRepository coordenadorRepository;
    private final usuarioRepository usuarioRepository;
    private final coordenadorMapper coordenadorMapper;
    private final usuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public coordenadorResponseDTO criarCoordenador(coordenadorRequestDTO request) {
        coordenador coordenadorModel = coordenadorMapper.toModel(request);
        usuario usuarioModel = coordenadorModel.getUsuario();

        if (usuarioRepository.existsByEmail(usuarioModel.getEmail())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }

        usuarioModel.setTipoUsuario(roleUsuario.COORDENADOR);
        usuarioModel.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));

        usuarioEntity usuarioEntity = usuarioMapper.toEntity(usuarioModel);
        usuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);
        coordenadorModel.setUsuario(usuarioMapper.toModel(usuarioSalvo));

        coordenadorEntity entity = coordenadorMapper.toEntity(coordenadorModel);
        coordenadorEntity salvo = coordenadorRepository.save(entity);
        return coordenadorMapper.toResponse(salvo);
    }

    public coordenadorResponseDTO buscarCoordenadorPorId(Long id) {
        coordenadorEntity entity = coordenadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));
        return coordenadorMapper.toResponse(entity);
    }

    public List<coordenadorResponseDTO> listarCoordenadores() {
        return coordenadorMapper.toResponseList(coordenadorRepository.findAll());
    }

    public List<coordenadorResponseDTO> buscarCoordenadoresPorNome(String nome) {
        return coordenadorMapper.toResponseList(coordenadorRepository.buscarPorNomeUsuario(nome));
    }

    public List<coordenadorResponseDTO> buscarPorCurso(Long cursoId) {
        return coordenadorMapper.toResponseList(coordenadorRepository.findByCursoId(cursoId));
    }

    @Transactional
    public coordenadorResponseDTO atualizarCoordenador(Long id, coordenadorRequestDTO request) {
        coordenadorEntity entity = coordenadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));

        usuarioEntity usuarioEntity = entity.getUsuario();
        if (usuarioRepository.existsByEmailAndIdNot(request.usuario().email(), usuarioEntity.getId())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }

        usuarioEntity.setNome(request.usuario().nome());
        usuarioEntity.setEmail(request.usuario().email());
        usuarioEntity.setSenha(passwordEncoder.encode(request.usuario().senha()));
        usuarioEntity.setTipoUsuario(roleUsuario.COORDENADOR);

        usuarioEntity usuarioAtualizado = usuarioRepository.save(usuarioEntity);

        entity.setUsuario(usuarioAtualizado);
        entity.setCursoId(request.cursoId());

        coordenadorEntity atualizado = coordenadorRepository.save(entity);
        return coordenadorMapper.toResponse(atualizado);
    }

    @Transactional
    public void deletarCoordenador(Long id) {
        coordenadorEntity entity = coordenadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));

        Long usuarioId = entity.getUsuario().getId();
        coordenadorRepository.delete(entity);
        usuarioRepository.deleteById(usuarioId);
    }
}
