package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.enums.RoleUsuario;
import PortalTADS.usuarioService.exception.AcessoProibidoException;
import PortalTADS.usuarioService.exception.EmailDuplicadoException;
import PortalTADS.usuarioService.exception.UsuarioNaoEncontradoException;
import PortalTADS.usuarioService.model.Usuario;
import PortalTADS.usuarioService.model.dto.usuario.UsuarioRequestDTO;
import PortalTADS.usuarioService.model.dto.usuario.UsuarioResponseDTO;
import PortalTADS.usuarioService.model.mapper.UsuarioMapper;
import PortalTADS.usuarioService.repository.entity.UsuarioEntity;
import PortalTADS.usuarioService.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioMapper mapper;
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO record) {
        if (repository.existsByEmail(record.email())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }


        Usuario usuarioModel = mapper.toModel(record);
        usuarioModel.setTipoUsuario(RoleUsuario.ADMIN);
        usuarioModel.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));


        UsuarioEntity entity = mapper.toEntity(usuarioModel);
        repository.save(entity);
        return mapper.toResponse(entity);
    }

    public UsuarioResponseDTO buscarUsuario(Long id) {
        UsuarioEntity entity = repository.findById(id).orElseThrow(()
                -> new UsuarioNaoEncontradoException(id));
        return mapper.toResponse(entity);
    }

    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO record) {
        UsuarioEntity entity = repository.findById(id).orElseThrow(()
                -> new UsuarioNaoEncontradoException(id));

        if (repository.existsByEmailAndIdNot(record.email(), id)) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }

        Usuario usuarioModel = mapper.toModel(record);
        usuarioModel.setId(entity.getId());
        usuarioModel.setTipoUsuario(RoleUsuario.ADMIN);
        UsuarioEntity updatedEntity = mapper.toEntity(usuarioModel);
        repository.save(updatedEntity);
        return mapper.toResponse(updatedEntity);
    }

    public void deletar(Long id) {

        if(!repository.existsById(id)) {
            throw new UsuarioNaoEncontradoException(id);
        }

        repository.deleteById(id);
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        List<UsuarioEntity> entidades = repository.findAll();

        List<UsuarioResponseDTO> listaUsuarios = mapper.toResponseList(entidades);
        return listaUsuarios;
    }


    public List<UsuarioResponseDTO> buscarUsuariosPorNome(String nome) {
        List<UsuarioEntity> entidades = repository.findByNomeContainingIgnoreCase(nome);
        return mapper.toResponseList(entidades);
    }

    public UsuarioResponseDTO atualizarPerfilProprio(String subEmail, UsuarioRequestDTO record) {
        UsuarioEntity entity = repository.findByEmail(subEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(subEmail));

        if (!entity.getEmail().equals(subEmail)) {
            throw new AcessoProibidoException("Você não tem permissão para atualizar este perfil.");
        }

        if (repository.existsByEmailAndIdNot(record.email(), entity.getId())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }

        entity.setNome(record.nome());
        entity.setEmail(record.email());
        entity.setSenha(passwordEncoder.encode(record.senha()));

        repository.save(entity);
        return mapper.toResponse(entity);
    }

}
