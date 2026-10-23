package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.exception.EmailDuplicadoException;
import PortalTADS.usuarioService.exception.UsuarioNaoEncontradoException;
import PortalTADS.usuarioService.model.dto.usuario.usuarioRequestDTO;
import PortalTADS.usuarioService.model.dto.usuario.usuarioResponseDTO;
import PortalTADS.usuarioService.model.mapper.usuarioMapper;
import PortalTADS.usuarioService.model.usuario;
import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import PortalTADS.usuarioService.repository.usuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class usuarioService {

    private final usuarioMapper mapper;
    private final usuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public usuarioResponseDTO salvarUsuario(usuarioRequestDTO record) {
        if (repository.existsByEmail(record.email())) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }

        usuario usuarioModel = mapper.toModel(record);
        usuarioModel.setTipoUsuario(roleUsuario.ADMIN);
        usuarioModel.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));


        usuarioEntity entity = mapper.toEntity(usuarioModel);
        repository.save(entity);
        return mapper.toResponse(entity);
    }

    public usuarioResponseDTO buscarUsuario(Long id) {
        usuarioEntity entity = repository.findById(id).orElseThrow(()
                -> new UsuarioNaoEncontradoException("Administrador não encontrado"));
        return mapper.toResponse(entity);
    }

    public usuarioResponseDTO atualizarUsuario(Long id, usuarioRequestDTO record) {
        usuarioEntity entity = repository.findById(id).orElseThrow(()
                -> new UsuarioNaoEncontradoException("Administrador não encontrado"));

        if (repository.existsByEmailAndIdNot(record.email(), id)) {
            throw new EmailDuplicadoException("Email já cadastrado.");
        }

        usuario usuarioModel = mapper.toModel(record);
        usuarioModel.setId(entity.getId());
        usuarioModel.setTipoUsuario(roleUsuario.ADMIN);
        usuarioEntity updatedEntity = mapper.toEntity(usuarioModel);
        repository.save(updatedEntity);
        return mapper.toResponse(updatedEntity);
    }

    public void deletar(Long id) {

        if(!repository.existsById(id)) {
            throw new UsuarioNaoEncontradoException("Administrador não encontrado");
        }

        repository.deleteById(id);
    }

    public List<usuarioResponseDTO> listarUsuarios() {
        List<usuarioEntity> enitdades = repository.findAll();

        List<usuarioResponseDTO> listaUsuarios = mapper.toResponseList(enitdades);
        return listaUsuarios;
    }


    public List<usuarioResponseDTO> buscarUsuariosPorNome(String nome) {
        List<usuarioEntity> enitdades = repository.findByNomeContainingIgnoreCase(nome);
        return mapper.toResponseList(enitdades);
    }


}
